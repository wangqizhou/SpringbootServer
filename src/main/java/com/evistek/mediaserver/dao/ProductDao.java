package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@Component
public class ProductDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public ProductDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectProductCount() {
        return this.sqlSession.selectOne("selectProductCount");
    }

    public Product selectProductById(int id) {
        return this.sqlSession.selectOne("selectProductById", id);
    }

    public Product selectProductByName(String name) {
        return this.sqlSession.selectOne("selectProductByName", name);
    }

    public List<Product> selectProducts() {
        return this.sqlSession.selectList("selectProducts");
    }

    public List<Product> selectProducts(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectProducts", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Product> selectProductsByCategoryId(int categoryId) {
        return this.sqlSession.selectList("selectProductsByCategoryId", categoryId);
    }

    public List<Product> selectProductsByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectProductsByCategoryId", categoryId,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Product> selectProductsByCategoryName(String categoryName) {
        return this.sqlSession.selectList("selectProductsByCategoryName",categoryName);
    }

    public List<Product> selectProductsByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectProductsByCategoryName",categoryName,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Product> selectProductsByAudit(boolean audit) {
        return this.sqlSession.selectList("selectProductsByAudit", audit);
    }

    public List<Product> selectProductsByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectProductsByAudit", audit,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int insertProduct(Product product) {
        return this.sqlSession.insert("insertProduct", product);
    }

    public int deleteProduct(Product product) {
        return this.sqlSession.delete("deleteProduct", product);
    }

    public int deleteProductById(int id) {
        return this.sqlSession.delete("deleteProductById", id);
    }

    public int deleteProducts(List<Product> productList) {
        return this.sqlSession.delete("deleteProducts", productList);
    }

    public int updateProduct(Product product) {
        return this.sqlSession.update("updateProduct", product);
    }
}
