package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.CategoryDao;
import com.evistek.mediaserver.entity.Category;
import com.evistek.mediaserver.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/23.
 */
@Service
public class CategoryServiceImpl implements ICategoryService{
    private final CategoryDao categoryDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> findCategories() {
        return this.categoryDao.selectCategories();
    }

    @Override
    public List<Category> findCategories(int pageNumber, int pageSize) {
        return this.categoryDao.selectCategories(pageNumber, pageSize);
    }

    @Override
    public List<Category> findCategoriesByType(String type) {
        return this.categoryDao.selectCategoriesByType(type);
    }

    @Override
    public List<Category> findCategoriesByType(String type, int pageNumber, int pageSize) {
        return this.categoryDao.selectCategoriesByType(type, pageNumber, pageSize);
    }

    @Override
    public Category findCategoryById(int id) {
        return this.categoryDao.selectCategoryById(id);
    }

    @Override
    public List<String> findCategoryType() {
        return this.categoryDao.selectCategoryType();
    }

    @Override
    public int findCategoryNumber() {
        return this.categoryDao.selectCategoryNumber();
    }

    @Override
    public int findCategoryId(String name, String type) {
        return this.categoryDao.selectCategoryId(name, type);
    }

    @Override
    public Category addCategory(Category category) {
        int result = this.categoryDao.insertCategory(category);
        if (result == 1) {
            int id = this.categoryDao.selectCategoryId(category.getName(), category.getType());
            return this.categoryDao.selectCategoryById(id);
        } else {
            logger.error("fail to add category with name: " + category.getName() + " and type: " + category.getType());
            return null;
        }
    }

    @Override
    public int deleteCategory(Category category) {
        return this.categoryDao.deleteCategory(category);
    }

    @Override
    public int deleteCategoryById(int id) {
        return this.categoryDao.deleteCategoryById(id);
    }

    @Override
    public int deleteCategoryByType(String type) {
        return this.categoryDao.deleteCategoryByType(type);
    }

    @Override
    public int updateCategory(Category category) {
        return this.categoryDao.updateCategory(category);
    }
}
