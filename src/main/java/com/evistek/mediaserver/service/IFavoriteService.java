package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Favorite;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/27.
 */
public interface IFavoriteService {
    String ORDER_ID = "id";
    String ORDER_TIME = "time";

    int findFavoriteNumber();
    Favorite findFavoriteById(int id);
    Favorite findFavorite(int userId, int videoId);
    List<Favorite> findFavorites();
    List<Favorite> findFavorites(OrderBy orderBy);
    List<Favorite> findFavorites(int pageNumber, int pageSize);
    List<Favorite> findFavorites(int pageNumber, int pageSize, OrderBy orderBy);
    List<Favorite> findFavoritesByUserId(int userId);
    List<Favorite> findFavoritesByUserId(int userId, OrderBy orderBy);
    List<Favorite> findFavoritesByUserId(int userId, int pageNumber, int pageSize);
    List<Favorite> findFavoritesByUserId(int userId, int pageNumber, int pageSize, OrderBy orderBy);

    Favorite addFavorite(Favorite favorite);

    int deleteFavorite(Favorite favorite);
    int deleteFavoriteById(int id);
    int deleteFavoritesByUserId(int userId);
    int deleteFavorites(List<Favorite> favoriteList);

    int updateFavorite(Favorite favorite);
}
