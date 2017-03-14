package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.service.IImageService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/5.
 */

@RestController
public class ImageController {

    private final Logger logger;
    private final IImageService imageService;
    private final OpLogger opLogger;

    public ImageController (OpLogger opLogger, IImageService imageService) {
        this.imageService = imageService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public String getImages(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<Image> images = imageService.findImages(pageNumber, pageSize, new OrderBy(IImageService.ORDER_DOWNLOAD, OrderBy.DESC));
        int total = imageService.findImageNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query images: " + images.size());

        return TablePageHelper.toJSONString(total, images);
    }

    @RequestMapping(value = "/images/number", method = RequestMethod.GET)
    public String getImageNumber(@RequestParam(value = "audit", required = false) Boolean audit,
                                 HttpServletRequest request) {

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query image number");

        if (audit != null) {
            return JSON.toJSONString(this.imageService.findImageNumberByAudit(audit));
        } else {
            return JSON.toJSONString(this.imageService.findImageNumber());
        }
    }

    @RequestMapping(value = "/images", method = RequestMethod.PUT)
    public String updateImage(@RequestBody Image image, HttpServletRequest request){
        String msg = "success";
        if (this.imageService.updateImage(image) != 1) {
            logger.error("updateImage failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_RESOURCE,
                "update information for image: " + image.getUrl());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/images", method = RequestMethod.DELETE)
    public String deleteImage(@RequestBody String id, HttpServletRequest request){
        String msg = "success";
        Image image = imageService.findImageById(Integer.parseInt(id));
        this.imageService.deleteFromDisk(image);
        if (this.imageService.deleteImage(image) != 1) {
            logger.error("deleteImage failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_DELETE_RESOURCE,
                "delete image: " + image.getUrl());

        return JSON.toJSONString(msg);
    }
}
