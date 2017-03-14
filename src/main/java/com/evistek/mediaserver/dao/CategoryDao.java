package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Category;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/23.
 */
@Component
public class CategoryDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public CategoryDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public List<Category> selectCategories() {
        return this.sqlSession.selectList("selectCategories");
    }

    public List<Category> selectCategories(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectCategories", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Category> selectCategoriesByType(String type) {
        return this.sqlSession.selectList("selectCategoriesByType", type);
    }

    public List<Category> selectCategoriesByType(String type, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectCategoriesByType", type,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public Category selectCategoryById(int id) {
        return this.sqlSession.selectOne("selectCategoryById", id);
    }

    public int selectCategoryId(String name, String type) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("name", name);
        parameterMap.put("type", type);
        Integer result = this.sqlSession.selectOne("selectCategoryId", parameterMap);
        if (result != null) {
            return result;
        }

        return 0;
    }

    public List<String> selectCategoryType() {
        return this.sqlSession.selectList("selectCategoryType");
    }

    public int selectCategoryNumber() {
        return this.sqlSession.selectOne("selectCategoryNumber");
    }

    public int insertCategory(Category category) {
        return this.sqlSession.insert("insertCategory", category);
    }

    public int deleteCategory(Category category) {
        return this.sqlSession.delete("deleteCategory", category);
    }

    public int deleteCategoryById(int id) {
        return this.sqlSession.delete("deleteCategoryById", id);
    }

    public int deleteCategoryByType(String type) {
        return this.sqlSession.delete("deleteCategoryByType", type);
    }

    public int updateCategory(Category category) {
        return this.sqlSession.update("updateCategory", category);
    }
}
