package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.IImageService;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.OpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/9.
 */

@RestController
public class AuditController {

    private final Logger logger;
    private final IImageService imageService;
    private final IVideoService videoService;
    private final IProductService productService;
    private final OpLogger opLogger;

    public AuditController (OpLogger opLogger, IImageService imageService, IVideoService videoService, IProductService productService) {
        this.imageService = imageService;
        this.videoService = videoService;
        this.productService = productService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/audit/image", method = RequestMethod.GET)
    public String getUnAuditImage(@RequestParam(value = "page") int pageNumber,
                                  @RequestParam(value = "page_size") int pageSize,
                                  HttpServletRequest request){
        List<Image> images = imageService.findImagesByAudit(false, pageNumber, pageSize);

        this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                "get unaudited images: " + images.size());
        return JSON.toJSONStringWithDateFormat(images, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/audit/image/id", method = RequestMethod.PUT)
    public String auditImageById(@RequestBody List<Integer> images, HttpServletRequest request) {
        String msg = "success";
        for (int i = 0 ; i < images.size() ; i++){
            Image image = imageService.findImageById(images.get(i));
            image.setAudit(true);
            if (imageService.updateImage(image) != 1) {
                logger.error("updateImage failed.");
                msg = "fail";
            }

            this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "approved image: " + image.getUrl());
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/audit/image/id", method = RequestMethod.DELETE)
    public String deleteUnAuditImageById(@RequestBody List<Integer> images, HttpServletRequest request){
        String msg = "success";
        for (int i = 0 ; i < images.size() ; i++) {
            Image image = imageService.findImageById(images.get(i));
            this.imageService.deleteFromDisk(image);
            if (imageService.deleteImage(image) != 1) {
                logger.error("deleteImage failed.");
                msg = "fail";
            }

            this.opLogger.warn(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "delete the unapproved image: " + image.getUrl());
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/audit/video", method = RequestMethod.GET)
    public String getUnAuditVideo(@RequestParam(value = "page") int pageNumber,
                                  @RequestParam(value = "page_size") int pageSize,
                                  HttpServletRequest request){
        List<Video> videos = videoService.findVideosByAudit(false, pageNumber, pageSize);

        this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                "get unaudited videos: " + videos.size());

        return JSON.toJSONStringWithDateFormat(videos, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/audit/video/id", method = RequestMethod.PUT)
    public String auditVideoById(@RequestBody List<Integer> videos, HttpServletRequest request) {
        String msg = "success";
        for (int i = 0 ; i < videos.size() ; i++) {
            Video video = videoService.findVideoById(videos.get(i));
            video.setAudit(true);
            if (videoService.updateVideo(video) != 1) {
                logger.error("updateVideo failed.");
                msg = "fail";
            }

            this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "approved video: " + video.getUrl());
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/audit/video/id", method = RequestMethod.DELETE)
    public String deleteUnAuditVideoById(@RequestBody List<Integer> videos, HttpServletRequest request){
        String msg = "success";
        for (int i = 0 ; i < videos.size() ; i++){
            Video video = videoService.findVideoById(videos.get(i));
            this.videoService.deleteVideoFromDisk(video);
            if (videoService.deleteVideo(video) != 1) {
                logger.error("deleteVideo failed.");
                msg = "fail";
            }

            this.opLogger.warn(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "delete the unapproved video: " + video.getUrl());
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/audit/product", method = RequestMethod.GET)
    public String getUnAuditProduct(@RequestParam(value = "page") int pageNumber,
                                  @RequestParam(value = "page_size") int pageSize,
                                    HttpServletRequest request){
        List<Product> products = productService.findProductsByAudit(false, pageNumber, pageSize);

        this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                "get unaudited products(ads): " + products.size());

        return JSON.toJSONStringWithDateFormat(products, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/audit/product/id", method = RequestMethod.PUT)
    public String auditProductById(@RequestBody List<Integer> products, HttpServletRequest request) {
        String msg = "success";
        for (int i = 0 ; i < products.size() ; i++) {
            Product product = productService.findProductById(products.get(i));
            product.setAudit(true);
            if (productService.updateProduct(product) != 1) {
                logger.error("updateProduct failed.");
                msg = "fail";
            }

            this.opLogger.info(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "approved product(ad): " + product.getImgUrl());
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/audit/product/id", method = RequestMethod.DELETE)
    public String deleteUnAuditProductById(@RequestBody List<Integer> products, HttpServletRequest request){
        String msg = "success";
        for (int i = 0 ; i < products.size() ; i++){
            Product product = productService.findProductById(products.get(i));
            this.productService.deleteProductFromDisk(product);
            if (productService.deleteProduct(product) != 1) {
                logger.error("deleteProduct failed.");
                msg = "fail";
            }

            this.opLogger.warn(request, OpLogger.ACTION_AUDIT_RESOURCE,
                    "delete the unapproved product(ad): " + product.getImgUrl());
        }
        return JSON.toJSONString(msg);
    }
}
