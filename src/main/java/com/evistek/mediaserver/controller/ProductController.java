package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/17.
 */

@RestController
public class ProductController {

    private final Logger logger;
    private final IProductService productService;
    private final OpLogger opLogger;

    public ProductController (OpLogger opLogger, IProductService productService) {
        this.productService = productService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getProducts(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<Product> products = productService.findProducts(pageNumber, pageSize);
        int total = productService.findProductNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query products(ads): " + products.size());

        return TablePageHelper.toJSONString(total, products);
    }

    @RequestMapping(value = "/products/number", method = RequestMethod.GET)
    public String getProductNumber(@RequestParam(value = "audit", required = false) Boolean audit,
                                   HttpServletRequest request) {

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query product number with audit status: " + audit);

        if (audit != null) {
            return JSON.toJSONString(this.productService.findProductsByAudit(audit).size());
        } else {
            return JSON.toJSONString(this.productService.findProductNumber());
        }
    }

    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    public String updateProduct(@RequestBody Product product, HttpServletRequest request){
        String msg = "success";
        if (this.productService.updateProduct(product) != 1) {
            logger.error("updateProduct failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_RESOURCE,
                "update product information with url: " + product.getImgUrl());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/products", method = RequestMethod.DELETE)
    public String deleteProduct(@RequestBody String id, HttpServletRequest request){
        String msg = "success";
        Product product = productService.findProductById(Integer.parseInt(id));
        this.productService.deleteProductFromDisk(product);
        if (this.productService.deleteProduct(product) != 1) {
            logger.error("deleteProduct failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_RESOURCE,
                "delete product with url: " + product.getImgUrl());

        return JSON.toJSONString(msg);
    }
}
