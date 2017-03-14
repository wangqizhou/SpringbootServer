package com.evistek.mediaserver.controller;

import com.evistek.mediaserver.entity.Favorite;
import com.evistek.mediaserver.service.IFavoriteService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/4.
 */

@RestController
public class FavoriteController {

    private final IFavoriteService favoriteService;
    private final Logger logger;
    private final OpLogger opLogger;

    public FavoriteController (IFavoriteService favoriteService, OpLogger opLogger) {
        this.favoriteService = favoriteService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public String getAllFavorites(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<Favorite> favorites = favoriteService.findFavorites(pageNumber, pageSize);
        int total = favoriteService.findFavoriteNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_FAVORITE, "query favorites: " + favorites.size());

        return TablePageHelper.toJSONString(total, favorites);
    }
}
