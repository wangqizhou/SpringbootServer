package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Favorite;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/27.
 */
@Component
public class FavoriteDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public FavoriteDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectFavoriteCount() {
        return this.sqlSession.selectOne("selectFavoriteCount");
    }

    public Favorite selectFavoriteById(int id) {
        return this.sqlSession.selectOne("selectFavoriteById", id);
    }

    public Favorite selectFavorite(int userId, int videoId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("videoId", videoId);

        return this.sqlSession.selectOne("selectFavorite", map);
    }

    public List<Favorite> selectFavorites() {
        return this.sqlSession.selectList("selectFavorites");
    }

    public List<Favorite> selectFavorites(OrderBy orderBy) {
        return this.sqlSession.selectList("selectFavoritesOrderBy", orderBy);
    }

    public List<Favorite> selectFavorites(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectFavorites", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Favorite> selectFavorites(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.sqlSession.selectList("selectFavoritesOrderBy", orderBy,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Favorite> selectFavoritesByUserId(int userId) {
        return this.sqlSession.selectList("selectFavoritesByUserId", userId);
    }

    public List<Favorite> selectFavoritesByUserId(int userId, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectFavoritesByUserIdOrderBy", map);
    }

    public List<Favorite> selectFavoritesByUserId(int userId, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectFavoritesByUserId", userId,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Favorite> selectFavoritesByUserId(int userId, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());

        return this.sqlSession.selectList("selectFavoritesByUserIdOrderBy", map,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int insertFavorite(Favorite favorite) {
        return this.sqlSession.insert("insertFavorite", favorite);
    }

    public int deleteFavorite(Favorite favorite) {
        return this.sqlSession.delete("deleteFavorite", favorite);
    }

    public int deleteFavoriteById(int id) {
        return this.sqlSession.delete("deleteFavoriteById", id);
    }

    public int deleteFavoritesByUserId(int userId) {
        return this.sqlSession.delete("deleteFavoritesByUserId", userId);
    }

    public int deleteFavorites(List<Favorite> favoriteList) {
        return this.sqlSession.delete("deleteFavorites", favoriteList);
    }

    public int updateFavorite(Favorite favorite) {
        return this.sqlSession.update("updateFavorite", favorite);
    }
}
