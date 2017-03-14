package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Category;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/23.
 */
public interface ICategoryService {
    List<Category> findCategories();
    List<Category> findCategories(int pageNumber, int pageSize);

    /**
     * 获取具有相同type的所有分类
     *
     * @param type 需要查找的type，比如video
     * @return 符合条件所有分类
     */
    List<Category> findCategoriesByType(String type);
    List<Category> findCategoriesByType(String type, int pageNumber, int pageSize);
    Category findCategoryById(int id);

    /**
     * 通过分类的name和type获取其id
     *
     * 在数据库中，分类的name和type都不是唯一的，但是对于某一种type而言，name是唯一的，
     * 因此，可以通过name和type唯一确定一个分类的id
     *
     * @param name 分类的名称
     * @param type 分类的类型
     * @return 返回分类的id，如果没找到，返回0
     */
    int findCategoryId(String name, String type);
    List<String> findCategoryType();
    int findCategoryNumber();

    Category addCategory(Category category);

    int deleteCategory(Category category);
    int deleteCategoryById(int id);

    /**
     * 删除某一类型的所有分类
     *
     * @param type 要删除的类型，比如video
     * @return 实际删除的分类个数
     */
    int deleteCategoryByType(String type);


    /**
     * 更新分类的属性，由于name和type属性都不唯一，只根据id进行更新
     *
     * @param category 新的分类，name和type属性如果不是null，就更新相应的值。为null，就不更新该属性
     * @return 更新成功返回1，否则返回0。 即便给定的name或者type值和数据库中的值相同，也认为更新成功，返回1
     */
    int updateCategory(Category category);
}
