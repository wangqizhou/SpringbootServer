package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/4.
 */
@RestController
@RequestMapping("/api/v2")
public class CategoryApi {

    private final ICategoryService categoryService;
    private final Logger logger;

    public CategoryApi(ICategoryService categoryService) {
        this.categoryService = categoryService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String getCategories() {
        return JSON.toJSONString(this.categoryService.findCategories());
    }

    @RequestMapping(value = "/categories/id/{id}", method = RequestMethod.GET)
    public String getCategoryById(@PathVariable("id") int id, HttpServletResponse resp) {
        if (id <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                         "message: " + "invalid value for id: " + id);
            return HttpErrorMessage.build(resp,
                            HttpServletResponse.SC_NOT_ACCEPTABLE,
                            "invalid value for id");
        }

        return JSON.toJSONString(this.categoryService.findCategoryById(id));
    }

    @RequestMapping(value = "/categories/type/{type}", method = RequestMethod.GET)
    public String getCategoriesByType(@PathVariable("type") String type, HttpServletResponse resp) {
        List<String> types = this.categoryService.findCategoryType();
        if (types.contains(type)) {
            return JSON.toJSONString(this.categoryService.findCategoriesByType(type));
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                         "message: " + "invalid value for type: " + type);
            return HttpErrorMessage.build(resp,
                            HttpServletResponse.SC_NOT_ACCEPTABLE,
                            "invalid value for type");
        }
    }
}
