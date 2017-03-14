package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Product;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
public interface IProductService {
    int findProductNumber();
    Product findProductById(int id);
    Product findProductByName(String name);
    List<Product> findProducts();
    List<Product> findProducts(int pageNumber, int pageSize);
    List<Product> findProductsByCategoryId(int categoryId);
    List<Product> findProductsByCategoryId(int categoryId, int pageNumber, int pageSize);
    List<Product> findProductsByCategoryName(String categoryName);
    List<Product> findProductsByCategoryName(String categoryName, int pageNumber, int pageSize);
    List<Product> findProductsByAudit(boolean audit);
    List<Product> findProductsByAudit(boolean audit, int pageNumber, int pageSize);

    Product addProduct(Product product);

    int deleteProduct(Product product);
    int deleteProductById(int id);
    int deleteProducts(List<Product> productList);

    /**
     * 从磁盘上删除产品(广告)文件
     *
     * @param product 所要删除的产品
     * @return 返回实际删除的文件个数。目前每一个产品(广告)只有一个图片，如果删除，则返回1，错误则返回0
     */
    int deleteProductFromDisk(Product product);
    int updateProduct(Product product);
}
