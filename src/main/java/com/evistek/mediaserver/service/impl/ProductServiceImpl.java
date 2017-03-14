package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.ProductDao;
import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.service.IProductService;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@Service
public class ProductServiceImpl implements IProductService {
    private final ProductDao productDao;
    private final UrlBuilder urlBuilder;
    private final Logger logger;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, UrlBuilder urlBuilder) {
        this.productDao = productDao;
        this.urlBuilder = urlBuilder;

        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int findProductNumber() {
        return this.productDao.selectProductCount();
    }

    @Override
    public Product findProductById(int id) {
        return this.productDao.selectProductById(id);
    }

    @Override
    public Product findProductByName(String name) {
        return this.productDao.selectProductByName(name);
    }

    @Override
    public List<Product> findProducts() {
        return this.productDao.selectProducts();
    }

    @Override
    public List<Product> findProducts(int pageNumber, int pageSize) {
        return this.productDao.selectProducts(pageNumber, pageSize);
    }

    @Override
    public List<Product> findProductsByCategoryId(int categoryId) {
        return this.productDao.selectProductsByCategoryId(categoryId);
    }

    @Override
    public List<Product> findProductsByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.productDao.selectProductsByCategoryId(categoryId, pageNumber, pageSize);
    }

    @Override
    public List<Product> findProductsByCategoryName(String categoryName) {
        return this.productDao.selectProductsByCategoryName(categoryName);
    }

    @Override
    public List<Product> findProductsByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.productDao.selectProductsByCategoryName(categoryName, pageNumber, pageSize);
    }

    @Override
    public List<Product> findProductsByAudit(boolean audit) {
        return this.productDao.selectProductsByAudit(audit);
    }

    @Override
    public List<Product> findProductsByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.productDao.selectProductsByAudit(audit, pageNumber, pageSize);
    }

    @Override
    public Product addProduct(Product product) {
        int result = this.productDao.insertProduct(product);
        if (result == 1) {
            return this.productDao.selectProductByName(product.getName());
        } else {
            logger.error("fail to add product with name: " + product.getName());
            return null;
        }
    }

    @Override
    public int deleteProduct(Product product) {
        return this.productDao.deleteProduct(product);
    }

    @Override
    public int deleteProductById(int id) {
        return this.productDao.deleteProductById(id);
    }

    @Override
    public int deleteProducts(List<Product> productList) {
        return this.productDao.deleteProducts(productList);
    }

    @Override
    public int deleteProductFromDisk(Product product) {
        File file = new File(this.urlBuilder.buildPhysicalPath(product));
        if (file.exists() && file.isFile()) {
            if(file.delete()) {
                this.logger.info("success to delete product from disk " + file);
                return 1;
            } else {
                this.logger.error("fail to delete product from disk " + file);
            }
        } else {
            this.logger.error("The product to be deleted from disk doesn't exist " + product.getName());
        }

        return 0;
    }

    @Override
    public int updateProduct(Product product) {
        Product productInDb = this.productDao.selectProductById(product.getId());

        if (productInDb.getCategoryId() != 0 && productInDb.getCategoryId() != product.getCategoryId()
                && productInDb.getCategoryName() != null
                && !productInDb.getCategoryName().equals(product.getCategoryName())) {
            // move file on the disk to the directory with category name
            moveTo(productInDb, product);
        }

        return this.productDao.updateProduct(product);
    }

    private void moveTo(Product from, Product to) {
        File toDir = new File(this.urlBuilder.buildPhysicalPath(to)).getParentFile();
        if (!toDir.exists()) {
            toDir.mkdirs();
        }

        File file = new File(this.urlBuilder.buildPhysicalPath(from));
        file.renameTo(new File(this.urlBuilder.buildPhysicalPath(to)));
        to.setImgUrl(this.urlBuilder.buildUrl(to));
    }
}
