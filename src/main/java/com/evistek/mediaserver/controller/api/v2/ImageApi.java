package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IImageService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by ymzhao on 2017/1/16.
 */
@RestController
@RequestMapping("/api/v2")
public class ImageApi {
    private final IImageService imageService;
    private final ICategoryService categoryService;
    private final Logger logger;

    public ImageApi(IImageService imageService, ICategoryService categoryService) {
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public String getImages(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                            @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
                            HttpServletResponse resp) {
        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        return JSON.toJSONStringWithDateFormat(this.imageService.findImages(pageNumber, pageSize,
                new OrderBy(IImageService.ORDER_CREATE_TIME, OrderBy.DESC), true),
                "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/images/id/{id}", method = RequestMethod.GET)
    public String getImageById(@PathVariable(value = "id") int id,
                               HttpServletResponse resp) {
        if (id <= 0 || id > this.imageService.findImageNumber()) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for id");
        }

        return JSON.toJSONStringWithDateFormat(this.imageService.findImageById(id, true),
                "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/images/category_id/{category_id}", method = RequestMethod.GET)
    public String getImagesByCategoryId(
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
                this.imageService.findImagesByCategoryId(categoryId,
                        pageNumber, pageSize, new OrderBy(IImageService.ORDER_CREATE_TIME, OrderBy.DESC), true),
                "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/images/category_name/{category_name}", method = RequestMethod.GET)
    public String getImagesByCategoryName(
            @PathVariable(value = "category_name") String categoryName,
            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
            HttpServletResponse resp) {

        int categoryId = this.categoryService.findCategoryId(categoryName, "image");
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
                this.imageService.findImagesByCategoryName(categoryName,
                        pageNumber, pageSize, new OrderBy(IImageService.ORDER_CREATE_TIME, OrderBy.DESC), true),
                "yyyy-MM-dd HH:mm:ss");
    }
}
