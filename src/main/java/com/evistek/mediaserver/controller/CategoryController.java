package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Category;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IImageService;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evis on 2017/1/3.
 */

@RestController
public class CategoryController {

    private final ICategoryService categoryService;
    private final IVideoService videoService;
    private final IImageService imageService;
    private final IProductService productService;
    private final Logger logger;
    private final OpLogger opLogger;

    public CategoryController(OpLogger opLogger, ICategoryService categoryService, IVideoService videoService,
                              IImageService imageService, IProductService productService) {
        this.categoryService = categoryService;
        this.videoService = videoService;
        this.imageService = imageService;
        this.productService = productService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String Statistic(HttpServletRequest request){
        List<Category> list = categoryService.findCategories();
        List<Statistic> statistic = new ArrayList<Statistic>();

        for(Category category : list) {
            Statistic statistics = new Statistic();
            statistics.setCategory(category.getName());
            statistics.setType(category.getType());
            switch (category.getType()) {
                case "video":
                    statistics.setNum(this.videoService.findVideosByCategoryId(category.getId()).size());
                    break;
                case "image":
                    statistics.setNum(this.imageService.findImagesByCategoryId(category.getId()).size());
                    break;
                case "product":
                    statistics.setNum(this.productService.findProductsByCategoryId(category.getId()).size());
                    break;
            }
            statistic.add(statistics);
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query resource count");

        return JSON.toJSONString(statistic);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String getCategories(HttpServletRequest request){
        List<Category> categories = categoryService.findCategories();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE,
                "query resource categories: " + categories.size());

        return JSON.toJSONString(categories);
    }

    @RequestMapping(value = "/categories/type/{type}", method = RequestMethod.GET)
    public String getCategoriesByType(@PathVariable(value = "type") String type, HttpServletRequest request) {
        List<String> types = this.categoryService.findCategoryType();
        if (types.contains(type)) {
            List<Category> categories = categoryService.findCategoriesByType(type);

            this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE,
                    "query " + categories.size() + " resource categories for type: " + type);

            return JSON.toJSONString(categories);
        }

        return null;
    }

    @RequestMapping(value = "/downloadCount", method = RequestMethod.GET)
    public ModelAndView toDownloadCountPage(){
        return new ModelAndView("downloadCount");
    }

    @RequestMapping(value = "/downloads", method = RequestMethod.GET)
    public String getDownloadCount(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "categoryId", required = false, defaultValue = "0") int id,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = offset / pageSize + 1;

        List<Download> downloads = new ArrayList<>();
        int total = 0;
        Category category = this.categoryService.findCategoryById(id);
        switch (category.getType()) {
            case "video":
                List<Video> videos = this.videoService.findVideosByCategoryId(id, pageNumber, pageSize,
                        new OrderBy(IVideoService.ORDER_DOWNLOAD, OrderBy.DESC));

                for(Video video: videos) {
                    Download download = new Download();
                    download.setName(video.getName());
                    download.setNum(String.valueOf(video.getDownloadCount()));
                    downloads.add(download);
                }
                total = this.videoService.findVideosByCategoryId(id).size();
                break;
            case "image":
                List<Image> images = this.imageService.findImagesByCategoryId(id, pageNumber, pageSize,
                        new OrderBy(IImageService.ORDER_DOWNLOAD, OrderBy.DESC));

                for(Image image: images) {
                    Download download = new Download();
                    download.setName(image.getName());
                    download.setNum(String.valueOf(image.getDownloadCount()));
                    downloads.add(download);
                }
                total = this.imageService.findImagesByCategoryId(id).size();
                break;
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_DOWNLOAD,
                "query download count for category: " + category.getName());

        return TablePageHelper.toJSONString(total, downloads);
    }

    class Download {
        private String name;
        private String num;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    class Statistic {
        private String type;
        private String category;
        private int num;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
