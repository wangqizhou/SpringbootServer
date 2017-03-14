package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by ymzhao on 2016/12/26.
 */
@Component
public class ImageDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public ImageDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public Image selectImageById(int id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return this.sqlSession.selectOne("selectImageById", map);
    }

    public Image selectImageById(int id, boolean audit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("audit", audit);
        return this.sqlSession.selectOne("selectImageById", map);
    }

    public Image selectImageByUrl(String url) {
        return this.sqlSession.selectOne("selectImageByUrl", url);
    }

    public List<Image> selectImages() {
        return this.sqlSession.selectList("selectImages");
    }

    public List<Image> selectImages(OrderBy orderBy) {
        return this.sqlSession.selectList("selectImagesOrderBy", orderBy);
    }

    public List<Image> selectImages(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectImages", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImages(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.sqlSession.selectList("selectImagesOrderBy", orderBy,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }


    public List<Image> selectImagesByCategoryId(int categoryId) {
        return this.sqlSession.selectList("selectImagesByCategoryId", categoryId);
    }

    public List<Image> selectImagesByCategoryId(int categoryId, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectImagesByCategoryIdOrderBy", map);
    }

    public List<Image> selectImagesByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectImagesByCategoryId", categoryId,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectImagesByCategoryIdOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        map.put("audit", audit);
        return this.sqlSession.selectList("selectImagesByCategoryIdOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByCategoryName(String categoryName) {
        return this.sqlSession.selectList("selectImagesByCategoryName", categoryName);
    }

    public List<Image> selectImagesByCategoryName(String categoryName, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectImagesByCategoryNameOrderBy", map);
    }

    public List<Image> selectImagesByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectImagesByCategoryName", categoryName,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectImagesByCategoryNameOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        map.put("audit", audit);
        return this.sqlSession.selectList("selectImagesByCategoryNameOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByAudit(boolean audit) {
        return this.sqlSession.selectList("selectImagesByAudit", audit);
    }

    public List<Image> selectImagesByAudit(boolean audit, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("audit", audit);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectImagesByAuditOrderBy", map);
    }

    public List<Image> selectImagesByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectImagesByAudit", audit,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Image> selectImagesByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("audit", audit);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectImagesByAuditOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int selectImageCount() {
        return this.sqlSession.selectOne("selectImageCount");
    }

    public int selectImageCountByAudit(boolean audit) {
        return this.sqlSession.selectOne("selectImageCountByAudit", audit);
    }


    public int insertImage(Image image) {
        return this.sqlSession.insert("insertImage", image);
    }

    public int deleteImage(Image image) {
        return this.sqlSession.delete("deleteImage", image);
    }

    public int deleteImageById(int id) {
        return this.sqlSession.delete("deleteImageById", id);
    }

    public int deleteImageByUrl(String url) {
        return this.sqlSession.delete("deleteImageByUrl", url);
    }

    public int deleteImages(List<Image> imageList) {
        return this.sqlSession.delete("deleteImages", imageList);
    }

    public int updateImage(Image image) {
        return this.sqlSession.update("updateImage", image);
    }
}
