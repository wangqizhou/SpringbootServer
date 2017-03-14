package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.IAdminService;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IEmailService;
import com.evistek.mediaserver.service.IUploadService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import com.evistek.mediaserver.utils.OpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/12.
 */
@RestController
public class UploadController {
    private final ICategoryService categoryService;
    private final IUploadService imageUploadService;
    private final IUploadService videoUploadService;
    private final IUploadService productUploadService;
    private final IAdminService adminService;
    private final IEmailService emailService;
    private final Logger logger;
    private final OpLogger opLogger;

    public UploadController(OpLogger opLogger, ICategoryService categoryService,
                            IAdminService adminService,
                            IEmailService emailService,
                            @Qualifier("UploadVideoService") IUploadService videoUploadService,
                            @Qualifier("UploadImageService") IUploadService imageUploadService,
                            @Qualifier("UploadProductService") IUploadService productUploadService) {
        this.imageUploadService = imageUploadService;
        this.videoUploadService = videoUploadService;
        this.categoryService = categoryService;
        this.adminService = adminService;
        this.productUploadService = productUploadService;
        this.emailService = emailService;
        this.logger = LoggerFactory.getLogger(getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/upload/images", method = RequestMethod.POST)
    public String uploadImages(@RequestPart(value = "imageFiles") MultipartFile file,
                               @RequestPart(value = "categoryName") String categoryName,
                               HttpServletRequest request,
                               HttpServletResponse resp) {

        int id = this.categoryService.findCategoryId(categoryName, "image");
        if (id > 0) {

            Image image = new Image();
            image.setOwnerId(getUserId(request));
            image.setCategoryId(id);
            image.setCategoryName(categoryName);
            int result = this.imageUploadService.save(file, image);
            if (result == -1) {
                logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " INTERNAL SERVER ERROR " +
                        "message: " + "fail to save file: " + file.getOriginalFilename());
                return HttpErrorMessage.build(resp,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "fail to save file: " + file.getOriginalFilename());
            } else {
                String message = "message: " + "success to save file: " + file.getOriginalFilename();
                logger.info("status:" + HttpServletResponse.SC_OK + " OK " + message);

                this.opLogger.info(request, OpLogger.ACTION_UPLOAD_RESOURCE,
                        "upload image with url: " + image.getUrl());

                Map<String, String> mailMessage = buildEmailMessage(image, request);
                emailService.sendEmail("图片", mailMessage);
                return JSON.toJSONString(message);
            }
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid category name: " + categoryName);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid category name: " + categoryName);
        }
    }

    @RequestMapping(value = "/upload/video", method = RequestMethod.POST)
    public String uploadVideo(@RequestPart("videoFiles") MultipartFile[] files,
                              @RequestPart("categoryName") String categoryName,
                              @RequestPart("properties") String json,
                              HttpServletRequest request,
                              HttpServletResponse resp) {

        Video video = JSON.parseObject(json, Video.class);
        if (video == null || this.categoryService.findCategoryId(categoryName, "video") == 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid video properties");
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE, "invalid video properties");
        }

        String filenames = "";
        for(MultipartFile file: files) {
            filenames += ("  " + file.getOriginalFilename());
        }

        video.setOwnerId(getUserId(request));
        video.setCategoryName(categoryName);
        int result = this.videoUploadService.save(files, video);
        if (result == 0) {
            String message = "success to save file: " + filenames;
            logger.info(message);

            this.opLogger.info(request, OpLogger.ACTION_UPLOAD_RESOURCE,
                    "upload video with url: " + video.getUrl());

            Map<String, String> mailMessage = buildEmailMessage(video, request);
            emailService.sendEmail("视频", mailMessage);
            return JSON.toJSONString(message);
        } else {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to save file: " + filenames);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to save file: " + filenames);
        }
    }

    @RequestMapping(value = "/upload/product", method = RequestMethod.POST)
    public String uploadProduct(@RequestPart("productFiles") MultipartFile file,
                                @RequestPart("categoryName") String categoryName,
                                @RequestPart("properties") String json,
                                HttpServletRequest request,
                                HttpServletResponse resp) {
        Product product = JSON.parseObject(json, Product.class);
        if (product == null || this.categoryService.findCategoryId(categoryName, "product") == 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid product properties");
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE, "invalid product properties");
        }

        product.setOwnerId(getUserId(request));
        product.setCategoryName(categoryName);
        int result = this.productUploadService.save(file, product);
        if (result == 0) {
            String message = "success to save file: " + file.getOriginalFilename();
            logger.info(message);

            this.opLogger.info(request, OpLogger.ACTION_UPLOAD_RESOURCE,
                    "upload product(ad) with url: " + product.getImgUrl());

            Map<String, String> mailMessage = buildEmailMessage(product, request);
            emailService.sendEmail("广告", mailMessage);
            return JSON.toJSONString(message);
        } else {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to save file: " + file.getOriginalFilename());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to save file: " + file.getOriginalFilename());
        }
    }

    private int getUserId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String username = (String) httpSession.getAttribute("username");
        Admin admin = this.adminService.findAdminByName(username);
        if (admin != null) {
            return admin.getId();
        } else {
            return 0;
        }
    }

    private Map<String, String> buildEmailMessage(Object resource, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        if (resource instanceof Video) {
            Video video = (Video) resource;
            map.put(IEmailService.MSG_TYPE, "资源类型: 视频");
            map.put(IEmailService.MSG_CATEGORY, "资源分类: " + video.getCategoryName());
            map.put(IEmailService.MSG_NAME, "视频名称: " + video.getName());
            map.put(IEmailService.MSG_FORMAT, "视频格式: " + video.getFormat());
            map.put(IEmailService.MSG_DURATION, "视频时长: " + video.getDuration());
            map.put(IEmailService.MSG_SIZE, "视频大小: " + video.getSize());
            map.put(IEmailService.MSG_WIDTH, "视频宽度: " + video.getWidth());
            map.put(IEmailService.MSG_HEIGHT, "视频高度: " + video.getHeight());
            map.put(IEmailService.MSG_URL, "视频URL: " + video.getUrl());
            map.put(IEmailService.MSG_LANDSCAPE_COVER, "横屏封面: " + video.getLandscapeCoverUrl());
            map.put(IEmailService.MSG_PORTRAIT_COVER, "竖屏封面: " + video.getPortraitCoverUrl());
            map.put(IEmailService.MSG_PRE_IMG1, "预览图1: " + video.getPreview1Url());
            map.put(IEmailService.MSG_PRE_IMG2, "预览图2: " + video.getPreview2Url());
            map.put(IEmailService.MSG_PRE_IMG3, "预览图3: " + video.getPreview3Url());
        } else if (resource instanceof Image) {
            Image image = (Image) resource;
            map.put(IEmailService.MSG_TYPE, "资源类型: 图片");
            map.put(IEmailService.MSG_CATEGORY, "资源分类: " + image.getCategoryName());
            map.put(IEmailService.MSG_NAME, "图片名称: " + image.getName());
            map.put(IEmailService.MSG_FORMAT, "图片格式: " + image.getFormat());
            map.put(IEmailService.MSG_SIZE, "图片大小: " + image.getSize());
            map.put(IEmailService.MSG_WIDTH, "图片宽度: " + image.getWidth());
            map.put(IEmailService.MSG_HEIGHT, "图片高度: " + image.getHeight());
            map.put(IEmailService.MSG_URL, "图片URL: " + image.getUrl());
            map.put(IEmailService.MSG_THUMBNAIL_URL, "缩略图URL: " + image.getThumbnail());
        } else if (resource instanceof Product) {
            Product product = (Product) resource;
            map.put(IEmailService.MSG_TYPE, "资源类型: 广告");
            map.put(IEmailService.MSG_CATEGORY, "资源分类: " + product.getCategoryName());
            map.put(IEmailService.MSG_NAME, "广告名称: " + product.getName());
            map.put(IEmailService.MSG_URL, "图片URL: " + product.getImgUrl());
            map.put(IEmailService.MSG_WEB_URL, "网页URL: " + product.getWebsiteUrl());
        }
        map.put(IEmailService.MSG_UPLOADER, "上传者: " + username);
        return map;
    }
}
