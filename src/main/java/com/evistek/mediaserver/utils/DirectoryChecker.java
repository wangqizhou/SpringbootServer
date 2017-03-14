package com.evistek.mediaserver.utils;

import com.evistek.mediaserver.entity.Category;
import com.evistek.mediaserver.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/17.
 */
@Component
public class DirectoryChecker {
    private final ICategoryService categoryService;
    private final String SOURCE_DIR;
    private final Logger logger;

    public DirectoryChecker(ICategoryService categoryService) {
        this.categoryService = categoryService;

        this.SOURCE_DIR = System.getProperty("catalina.base") + File.separator +
                "webapps" + File.separator + "ROOT";
        this.logger = LoggerFactory.getLogger(getClass());
    }

    public void check(String categoryType) {
        List<Category> categories = this.categoryService.findCategoriesByType(categoryType);
        for (Category c : categories) {
            String dir = new StringBuilder()
                    .append(SOURCE_DIR).append(File.separator)
                    .append(c.getType()).append(File.separator)
                    .append(c.getName())
                    .toString();
            File file = new File(dir);
            if (!file.exists()) {
                logger.info("Create dir: " + dir);
                file.mkdirs();
            }
        }
    }
}
