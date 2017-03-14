package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/4.
 */
@RestController
@RequestMapping("/api/v2")
public class ProductApi {
    private final IProductService productService;
    private final Logger logger;

    public ProductApi(IProductService productService) {
        this.productService = productService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                              @RequestParam(value = "page_size", required = false, defaultValue = "0") int pageSize,
                              HttpServletResponse resp) {
        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        return JSON.toJSONString(this.productService.findProducts(pageNumber, pageSize));
    }
}
