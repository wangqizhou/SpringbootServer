package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.service.IUploadService;
import com.evistek.mediaserver.utils.DirectoryChecker;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/17.
 */
@Service("UploadProductService")
public class UploadProductServiceImpl implements IUploadService{
    private final IProductService productService;
    private final ICategoryService categoryService;
    private final UrlBuilder urlBuilder;
    private final DirectoryChecker dirChecker;
    private final Logger logger;

    public UploadProductServiceImpl(IProductService productService, ICategoryService categoryService,
                                    UrlBuilder urlBuilder, DirectoryChecker dirChecker) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.urlBuilder = urlBuilder;
        this.dirChecker = dirChecker;

        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int save(MultipartFile file, Object object) {
        Product product = (Product) object;
        setProductInfo(product);

        if (saveFile(file, product) == 0) {
            if (this.productService.addProduct(product) == null) {
                File f = new File(this.urlBuilder.buildPhysicalPath(product));
                if (f.exists() && f.isFile()) {
                    f.delete();
                    return -1;
                }
            }

            return 0;
        }

        return -1;
    }

    @Override
    public int save(MultipartFile[] files, Object object) {
        return 0;
    }

    private void setProductInfo(Product product) {
        int categoryId = this.categoryService.findCategoryId(product.getCategoryName(), "product");

        product.setCategoryId(categoryId);
        product.setAudit(false);
        product.setImgUrl(this.urlBuilder.buildUrl(product));
    }

    private int saveFile(MultipartFile file, Product product) {
        File savedFile = new File(this.urlBuilder.buildPhysicalPath(product));
        try {
            this.dirChecker.check("product");
            file.transferTo(savedFile);
            this.logger.info("save file: " + file.getOriginalFilename() + " to " + savedFile);
        } catch (IOException e) {
            e.printStackTrace();

            if (savedFile.exists()) {
                savedFile.delete();
            }

            this.logger.error("fail to save file: " + file.getOriginalFilename() + " to " + savedFile);
            return -1;
        }

        return 0;
    }
}
