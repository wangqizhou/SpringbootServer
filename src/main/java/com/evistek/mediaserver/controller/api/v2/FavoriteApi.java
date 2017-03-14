package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Favorite;
import com.evistek.mediaserver.model.FavoriteModel;
import com.evistek.mediaserver.service.IFavoriteService;
import com.evistek.mediaserver.service.IUserService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/4.
 */
@RestController
@RequestMapping("/api/v2")
public class FavoriteApi {
    private final IFavoriteService favoriteService;
    private final IUserService userService;
    private final IVideoService videoService;
    private final Logger logger;

    public FavoriteApi(IFavoriteService favoriteService, IUserService userService, IVideoService videoService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.videoService = videoService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/favorites/user_id/{user_id}", method = RequestMethod.GET)
    public String getFavoritesByUserId(@PathVariable(value = "user_id") int userId,
                                       @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
                                       HttpServletResponse resp) {

        if (userId <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for user_id");
        }

        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        List<Favorite> favorites = this.favoriteService.findFavoritesByUserId(userId,
                pageNumber, pageSize, new OrderBy(IFavoriteService.ORDER_TIME, OrderBy.DESC));

        List<FavoriteModel> favoriteModels = new ArrayList<>();
        for (Favorite entity: favorites) {
            FavoriteModel model = new FavoriteModel();
            model.setId(entity.getId());
            model.setUserId(entity.getUserId());
            model.setTime(entity.getTime());
            model.setVideo(this.videoService.findVideoById(entity.getVideoId()));

            favoriteModels.add(model);
        }

        return JSON.toJSONStringWithDateFormat(favoriteModels, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/favorites", method = RequestMethod.POST)
    public String addFavorite(@RequestBody Favorite favorite, HttpServletResponse resp) {
        Favorite result = this.favoriteService.addFavorite(favorite);
        if (result == null) {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to add favorite with video id: " + favorite.getVideoId());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to add favorite with video id: " + favorite.getVideoId());
        }

        return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/favorites/id/{id}", method = RequestMethod.DELETE)
    public String deleteFavorite(@PathVariable(value = "id") int id, HttpServletResponse resp) {

        if (id <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for id");
        }

        int result = this.favoriteService.deleteFavoriteById(id);
        if (result == 1) {
            logger.info("status:" + HttpServletResponse.SC_OK + " OK " +
                    "message: " + "success to delete favorite with id: " + id);
            return JSON.toJSONString("success to delete favorite with id: " + id);
        } else {
            logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " INTERNAL SERVER ERROR " +
                    "message: " + "success to delete favorite with id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "fail to delete favorite with id: " + id);
        }
    }

    @RequestMapping(value = "/favorites/user_id/{user_id}", method = RequestMethod.DELETE)
    public String deleteFavoritesByUserId(@PathVariable(value = "user_id") int userId, HttpServletResponse resp) {
        if (userId <= 0 || userId > this.userService.findUserNumber()) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for user_id");
        }

        int result = this.favoriteService.deleteFavoritesByUserId(userId);
        if (result > 0) {
            logger.info("status:" + HttpServletResponse.SC_OK + " OK " +
                    "message: " + "success to delete " + result + " favorite(s) with user_id: " + userId);
            return JSON.toJSONString(
                    "success to delete " + result + " favorite(s) with user_id: " + userId);
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND " +
                    "message: " + "not found favorite with user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_FOUND,
                    "not found favorite with user_id: " + userId);
        }
    }

    @RequestMapping(value = "/favorites", method = RequestMethod.DELETE)
    public String deleteFavorites(@RequestBody List<Favorite> favoriteList, HttpServletResponse resp) {
        int result = this.favoriteService.deleteFavorites(favoriteList);
        if (result > 0) {
            logger.info("status:" + HttpServletResponse.SC_OK + "OK" + "message: " + "success to delete " + result +
                    " favorites with userId " + favoriteList.get(0).getUserId());
            return JSON.toJSONString("success to delete " + result +
                    " favorites with userId: " + favoriteList.get(0).getUserId());
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND " + "message:" + "not found " +
                    "favorites with userId: " + favoriteList.get(0).getUserId());
            return HttpErrorMessage.build(resp, HttpServletResponse.SC_NOT_FOUND, "not found favorites with " +
                    "userId" + favoriteList.get(0).getUserId());
        }
    }
}
