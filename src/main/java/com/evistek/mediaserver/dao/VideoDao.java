package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/25.
 */
@Component
public class VideoDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public VideoDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public Video selectVideoById(int id) {
        return this.sqlSession.selectOne("selectVideoById", id);
    }

    public Video selectVideoByUrl(String url) {
        return this.sqlSession.selectOne("selectVideoByUrl", url);
    }

    public List<Video> selectVideos() {
        return this.sqlSession.selectList("selectVideos");
    }

    public List<Video> selectVideos(OrderBy orderBy) {
        return this.sqlSession.selectList("selectVideosOrderBy", orderBy);
    }

    public List<Video> selectVideos(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectVideos", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideos(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.sqlSession.selectList("selectVideosOrderBy", orderBy,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryId(int categoryId) {
        return this.sqlSession.selectList("selectVideosByCategoryId", categoryId);
    }

    public List<Video> selectVideosByCategoryId(int categoryId, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByCategoryIdOrderBy", map);
    }

    public List<Video> selectVideosByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectVideosByCategoryId", categoryId,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByCategoryIdOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        map.put("audit", audit);

        return this.sqlSession.selectList("selectVideosByCategoryIdOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryName(String categoryName) {
        return this.sqlSession.selectList("selectVideosByCategoryName", categoryName);
    }

    public List<Video> selectVideosByCategoryName(String categoryName, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByCategoryNameOrderBy", map);
    }

    public List<Video> selectVideosByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectVideosByCategoryName", categoryName,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByCategoryNameOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", categoryName);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        map.put("audit", audit);

        return this.sqlSession.selectList("selectVideosByCategoryNameOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByAudit(boolean audit) {
        return this.sqlSession.selectList("selectVideosByAudit", audit);
    }

    public List<Video> selectVideosByAudit(boolean audit, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("audit", audit);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByAuditOrderBy", map);
    }

    public List<Video> selectVideosByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectVideosByAudit", audit,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Video> selectVideosByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("audit", audit);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectVideosByAuditOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int selectVideoCount() {
        return this.sqlSession.selectOne("selectVideoCount");
    }

    public int selectVideoCountByAudit(boolean audit) {
        return this.sqlSession.selectOne("selectVideoCountByAudit", audit);
    }

    public int insertVideo(Video video) {
        return this.sqlSession.insert("insertVideo", video);
    }

    public int deleteVideo(Video video) {
        return this.sqlSession.delete("deleteVideo", video);
    }

    public int deleteVideoById(int id) {
        return this.sqlSession.delete("deleteVideoById", id);
    }

    public int deleteVideoByUrl(String url) {
        return this.sqlSession.delete("deleteVideoByUrl", url);
    }

    public int deleteVideos(List<Video> videoList) {
        return this.sqlSession.delete("deleteVideos", videoList);
    }

    public int updateVideo(Video video) {
        return this.sqlSession.update("updateVideo", video);
    }

}
