package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/4.
 */
@RestController
@RequestMapping("/api/v2")
public class VideoApi {
    private final IVideoService videoService;
    private final ICategoryService categoryService;
    private final Logger logger;

    public VideoApi(IVideoService videoService, ICategoryService categoryService) {
        this.videoService = videoService;
        this.categoryService = categoryService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public String getVideos(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                            @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
                            HttpServletResponse resp) {
        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        return JSON.toJSONStringWithDateFormat(this.videoService.findVideosByAudit(true,
                    pageNumber, pageSize, new OrderBy(IVideoService.ORDER_CREATE_TIME, OrderBy.DESC)),
                    "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/videos/id/{id}", method = RequestMethod.GET)
    public String getVideoById(@PathVariable(value = "id") int id,
                               HttpServletResponse resp) {
        if (id <= 0 || id > this.videoService.findVideoNumber()) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for id");
        }

        return JSON.toJSONStringWithDateFormat(this.videoService.findVideoById(id), "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/videos/category_id/{category_id}", method = RequestMethod.GET)
    public String getVideosByCategoryId(
            @PathVariable(value = "category_id") int categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
            HttpServletResponse resp) {

        if (categoryId <= 0 || categoryId > this.categoryService.findCategoryNumber()) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for category_id: " + categoryId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for category_id");
        }

        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        return JSON.toJSONStringWithDateFormat(
                this.videoService.findVideosByCategoryId(categoryId,
                        pageNumber, pageSize, new OrderBy(IVideoService.ORDER_CREATE_TIME, OrderBy.DESC), true),
                "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/videos/category_name/{category_name}", method = RequestMethod.GET)
    public String getVideosByCategoryName(
            @PathVariable(value = "category_name") String categoryName,
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
            HttpServletResponse resp) {

        int categoryId = this.categoryService.findCategoryId(categoryName, "video");
        if (categoryId == 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for category_name: " + categoryName);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for category_name");
        }

        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        return JSON.toJSONStringWithDateFormat(
                this.videoService.findVideosByCategoryName(categoryName,
                        pageNumber, pageSize, new OrderBy(IVideoService.ORDER_CREATE_TIME, OrderBy.DESC), true),
                "yyyy-MM-dd HH:mm:ss");
    }
}
