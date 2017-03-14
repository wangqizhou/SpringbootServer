package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.FavoriteDao;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Favorite;
import com.evistek.mediaserver.service.IFavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.evistek.mediaserver.dao.OrderBy.ASC;
import static com.evistek.mediaserver.dao.OrderBy.DESC;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/27.
 */
@Service
public class FavoriteServiceImpl implements IFavoriteService {
    private final FavoriteDao favoriteDao;
    private final Logger logger;

    @Autowired
    public FavoriteServiceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int findFavoriteNumber() {
        return this.favoriteDao.selectFavoriteCount();
    }

    @Override
    public Favorite findFavoriteById(int id) {
        return this.favoriteDao.selectFavoriteById(id);
    }

    @Override
    public Favorite findFavorite(int userId, int videoId) {
        return this.favoriteDao.selectFavorite(userId, videoId);
    }

    @Override
    public List<Favorite> findFavorites() {
        return this.favoriteDao.selectFavorites();
    }

    @Override
    public List<Favorite> findFavorites(OrderBy orderBy) {
        return this.favoriteDao.selectFavorites(validateOrderBy(orderBy));
    }

    @Override
    public List<Favorite> findFavorites(int pageNumber, int pageSize) {
        return this.favoriteDao.selectFavorites(pageNumber, pageSize);
    }

    @Override
    public List<Favorite> findFavorites(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.favoriteDao.selectFavorites(pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Favorite> findFavoritesByUserId(int userId) {
        return this.favoriteDao.selectFavoritesByUserId(userId);
    }

    @Override
    public List<Favorite> findFavoritesByUserId(int userId, OrderBy orderBy) {
        return this.favoriteDao.selectFavoritesByUserId(userId, validateOrderBy(orderBy));
    }

    @Override
    public List<Favorite> findFavoritesByUserId(int userId, int pageNumber, int pageSize) {
        return this.favoriteDao.selectFavoritesByUserId(userId, pageNumber, pageSize);
    }

    @Override
    public List<Favorite> findFavoritesByUserId(int userId, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.favoriteDao.selectFavoritesByUserId(userId, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public Favorite addFavorite(Favorite favorite) {
        int result = 0;

        Favorite f = this.favoriteDao.selectFavorite(favorite.getUserId(), favorite.getVideoId());
        if (f != null) {
            result = this.favoriteDao.deleteFavorite(f);
            if (result != 1) {
                logger.error("fail to delete favorite." +
                        " id: " + f.getId() + " userId: " + f.getUserId() +
                        " videoId: " + f.getVideoId() + " videoName: " + f.getVideoName());
            }
        }

        favorite.setTime(new Date());
        result = this.favoriteDao.insertFavorite(favorite);
        if (result == 1) {
            return this.favoriteDao.selectFavorite(favorite.getUserId(), favorite.getVideoId());
        }

        return null;
    }

    @Override
    public int deleteFavorite(Favorite favorite) {
        return this.favoriteDao.deleteFavorite(favorite);
    }

    @Override
    public int deleteFavoriteById(int id) {
        return this.favoriteDao.deleteFavoriteById(id);
    }

    @Override
    public int deleteFavoritesByUserId(int userId) {
        return this.favoriteDao.deleteFavoritesByUserId(userId);
    }

    @Override
    public int deleteFavorites(List<Favorite> favoriteList) {
        return this.favoriteDao.deleteFavorites(favoriteList);
    }

    @Override
    public int updateFavorite(Favorite favorite) {
        return this.favoriteDao.updateFavorite(favorite);
    }

    private OrderBy validateOrderBy(OrderBy orderBy) {
        List<String> keyList = Arrays.asList(
                ORDER_ID,
                ORDER_TIME
         );

        List<String> orderList = Arrays.asList(
                ASC,
                DESC
        );

        if (!keyList.contains(orderBy.getKey()) || !orderList.contains(orderBy.getOrder())) {
            logger.error("invalid parameters for ORDER BY: " + orderBy.getKey() + " " + orderBy.getOrder() + "."
                    + " Use ORDER BY id ASC as default."
            );
            return new OrderBy(ORDER_ID, ASC);
        }

        return orderBy;
    }
}
