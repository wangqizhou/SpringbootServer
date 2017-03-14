package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-categories-data.sql"})
public class CategoryServiceTests {
    @Autowired
    private ICategoryService categoryService;

    @Before
    public void setup() {

    }

    @Test
    public void findCategory() {
        int result = this.categoryService.findCategoryNumber();
        assertThat(result).isEqualTo(10);

        List<Category> categoryList = this.categoryService.findCategories();
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(10);

        List<Category> categoryList1 = this.categoryService.findCategories(1, 5);
        assertThat(categoryList1).isNotNull();
        assertThat(categoryList1.size()).isEqualTo(5);

        List<Category> categoryList2 = this.categoryService.findCategories(2, 5);
        assertThat(categoryList2).isNotNull();
        assertThat(categoryList2.size()).isEqualTo(5);
        assertThat(categoryList2.get(0).getName()).isEqualTo("建筑");

        List<Category> categoryList3 = this.categoryService.findCategories(3, 5);
        assertThat(categoryList3).isNotNull();
        assertThat(categoryList3.size()).isEqualTo(0);

        List<Category> categoryList4 = this.categoryService.findCategoriesByType("video");
        assertThat(categoryList4).isNotNull();
        assertThat(categoryList4.size()).isEqualTo(3);

        List<Category> categoryList5 = this.categoryService.findCategoriesByType("video", 1, 2);
        assertThat(categoryList5).isNotNull();
        assertThat(categoryList5.size()).isEqualTo(2);

        List<Category> categoryList6 = this.categoryService.findCategoriesByType("video", 2, 2);
        assertThat(categoryList6).isNotNull();
        assertThat(categoryList6.size()).isEqualTo(1);

        Category category = this.categoryService.findCategoryById(1);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo("电影");
        assertThat(category.getType()).isEqualTo("video");

        int id = this.categoryService.findCategoryId("电影", "video");
        assertThat(id).isEqualTo(1);
        int id1 = this.categoryService.findCategoryId("人物", "image");
        assertThat(id1).isEqualTo(5);
        int id2 = this.categoryService.findCategoryId("电影", "image");
        assertThat(id2).isEqualTo(0);

        List<String> typeList = this.categoryService.findCategoryType();
        assertThat(typeList.size()).isEqualTo(3);
        assertThat(typeList.get(0)).isEqualTo("image");
        assertThat(typeList.get(1)).isEqualTo("product");
        assertThat(typeList.get(2)).isEqualTo("video");
    }

    @Test
    public void addCategory() {
        int result = this.categoryService.findCategoryNumber();
        assertThat(result).isEqualTo(10);

        Category tvCategory = new Category();
        tvCategory.setName("TVShow");
        tvCategory.setType("video");
        Category newCategory = this.categoryService.addCategory(tvCategory);
        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getName()).isEqualTo("TVShow");

        Category lomoCategory = new Category();
        lomoCategory.setName("LOMO");
        lomoCategory.setType("image");
        Category newCategory1 = this.categoryService.addCategory(lomoCategory);
        assertThat(newCategory1).isNotNull();
        assertThat(newCategory1.getName()).isEqualTo("LOMO");

        result = this.categoryService.findCategoryNumber();
        assertThat(result).isEqualTo(12);

        tvCategory = this.categoryService.findCategoryById(11);
        assertThat(tvCategory).isNotNull();
        assertThat(tvCategory.getName()).isEqualTo("TVShow");
        assertThat(tvCategory.getType()).isEqualTo("video");

        lomoCategory = this.categoryService.findCategoryById(12);
        assertThat(lomoCategory).isNotNull();
        assertThat(lomoCategory.getName()).isEqualTo("LOMO");
        assertThat(lomoCategory.getType()).isEqualTo("image");
    }

    @Test
    public void deleteCategory() {
        Category category = this.categoryService.findCategoryById(1);
        assertThat(category).isNotNull();

        int result = this.categoryService.deleteCategory(category);
        assertThat(result).isEqualTo(1);
        category = this.categoryService.findCategoryById(1);
        assertThat(category).isNull();

        category = this.categoryService.findCategoryById(2);
        assertThat(category).isNotNull();
        result = this.categoryService.deleteCategoryById(2);
        assertThat(result).isEqualTo(1);
        category = this.categoryService.findCategoryById(2);
        assertThat(category).isNull();

        List<Category> categoryList = this.categoryService.findCategoriesByType("image");
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(4);
        result = this.categoryService.deleteCategoryByType("image");
        assertThat(result).isEqualTo(4);
        categoryList = this.categoryService.findCategoriesByType("image");
        assertThat(categoryList.size()).isEqualTo(0);
    }

    @Test
    public void updateCategory() {
        Category category = this.categoryService.findCategoryById(1);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo("电影");
        assertThat(category.getType()).isEqualTo("video");

        category.setName("movies");
        category.setType("Movie");
        int result = this.categoryService.updateCategory(category);
        assertThat(result).isEqualTo(1);

        category = this.categoryService.findCategoryById(1);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo("movies");
        assertThat(category.getType()).isEqualTo("Movie");

        category.setType(null);
        result = this.categoryService.updateCategory(category);
        assertThat(result).isEqualTo(1);
        category = this.categoryService.findCategoryById(1);
        assertThat(category).isNotNull();
        assertThat(category.getType()).isEqualTo("Movie");
    }
}
