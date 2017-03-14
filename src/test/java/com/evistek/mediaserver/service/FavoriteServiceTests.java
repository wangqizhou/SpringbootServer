package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Favorite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql",
        "classpath:test-users-data.sql",
        "classpath:test-videos-data.sql",
        "classpath:test-favorites-data.sql"})
public class FavoriteServiceTests {
    @Autowired
    private IFavoriteService favoriteService;

    @Before
    public void setup() {

    }

    @Test
    public void findFavorite() {
        int result  = this.favoriteService.findFavoriteNumber();
        assertThat(result).isEqualTo(34);

        Favorite favorite = this.favoriteService.findFavoriteById(1);
        assertThat(favorite).isNotNull();
        assertThat(favorite.getVideoId()).isEqualTo(13);
        assertThat(favorite.getVideoName()).isEqualTo("挪威.卑尔根");
        assertThat(favorite.getUserId()).isEqualTo(8);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            assertThat(favorite.getTime()).isEqualTo(formatter.parse("2016-08-17 11:35:45"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Favorite> favoriteList = this.favoriteService.findFavoritesByUserId(2);
        assertThat(favoriteList).isNotNull();
        assertThat(favoriteList.size()).isEqualTo(5);

        List<Favorite> favoriteList1 = this.favoriteService.findFavoritesByUserId(2, 1, 2);
        assertThat(favoriteList1).isNotNull();
        assertThat(favoriteList1.size()).isEqualTo(2);

        List<Favorite> favoriteList2 = this.favoriteService.findFavoritesByUserId(2, 3, 2);
        assertThat(favoriteList2).isNotNull();
        assertThat(favoriteList2.size()).isEqualTo(1);

        List<Favorite> favoriteList3 = this.favoriteService.findFavorites();
        assertThat(favoriteList3).isNotNull();
        assertThat(favoriteList3.size()).isEqualTo(34);

        List<Favorite> favoriteList4 = this.favoriteService.findFavorites(1, 10);
        assertThat(favoriteList4).isNotNull();
        assertThat(favoriteList4.size()).isEqualTo(10);

        List<Favorite> favoriteList5 = this.favoriteService.findFavorites(4, 10);
        assertThat(favoriteList5).isNotNull();
        assertThat(favoriteList5.size()).isEqualTo(4);

        Favorite favorite1 = this.favoriteService.findFavorite(2, 21);
        assertThat(favorite1).isNotNull();
        assertThat(favorite1.getVideoName()).isEqualTo("柳如是");
    }

    @Test
    public void findFavoriteOrderBy() {
        List<Favorite> list = this.favoriteService.findFavorites(
                new OrderBy(IFavoriteService.ORDER_TIME,OrderBy.DESC));
        assertThat(list.size()).isEqualTo(34);
        assertThat(list.get(0).getId()).isEqualTo(34);

        List<Favorite> list1 = this.favoriteService.findFavorites(4, 10,
                new OrderBy(IFavoriteService.ORDER_TIME,OrderBy.DESC));
        assertThat(list1.size()).isEqualTo(4);
        assertThat(list1.get(0).getId()).isEqualTo(4);

        List<Favorite> list2 = this.favoriteService.findFavoritesByUserId(2,
                new OrderBy(IFavoriteService.ORDER_TIME,OrderBy.DESC));
        assertThat(list2.size()).isEqualTo(5);
        assertThat(list2.get(0).getId()).isEqualTo(23);

        List<Favorite> list3 = this.favoriteService.findFavoritesByUserId(2, 2, 3,
                new OrderBy(IFavoriteService.ORDER_TIME,OrderBy.DESC));
        assertThat(list3.size()).isEqualTo(2);
        assertThat(list3.get(0).getId()).isEqualTo(20);
    }

    @Test
    public void addFavorite() {
        int result  = this.favoriteService.findFavoriteNumber();
        assertThat(result).isEqualTo(34);

        Favorite favorite = new Favorite();
        favorite.setUserId(2);
        favorite.setVideoId(8);
        favorite.setVideoName("少女时代.Genie.mp4");
        favorite.setTime(new Date());

        Favorite favorite1 = this.favoriteService.addFavorite(favorite);
        assertThat(favorite1).isNotNull();
        result  = this.favoriteService.findFavoriteNumber();
        assertThat(result).isEqualTo(35);
        List<Favorite> favoriteList = this.favoriteService.findFavoritesByUserId(2);
        assertThat(favoriteList).isNotNull();
        assertThat(favoriteList.size()).isEqualTo(6);
    }

    @Test
    public void deleteFavorite() {
        Favorite favorite = this.favoriteService.findFavoriteById(1);
        assertThat(favorite).isNotNull();
        int result = this.favoriteService.deleteFavorite(favorite);
        assertThat(result).isEqualTo(1);
        Favorite favorite1 = this.favoriteService.findFavoriteById(1);
        assertThat(favorite1).isNull();

        Favorite favorite2 = this.favoriteService.findFavoriteById(2);
        assertThat(favorite2).isNotNull();
        result = this.favoriteService.deleteFavoriteById(2);
        assertThat(result).isEqualTo(1);
        Favorite favorite3 = this.favoriteService.findFavoriteById(2);
        assertThat(favorite3).isNull();

        List<Favorite> favoriteList = this.favoriteService.findFavoritesByUserId(2);
        assertThat(favoriteList).isNotNull();
        assertThat(favoriteList.size()).isEqualTo(5);
        result = this.favoriteService.deleteFavoritesByUserId(2);
        assertThat(result).isEqualTo(5);
        List<Favorite> favoriteList1 = this.favoriteService.findFavoritesByUserId(2);
        assertThat(favoriteList1.size()).isEqualTo(0);

        List<Favorite> favoriteList2 = this.favoriteService.findFavoritesByUserId(1);
        assertThat(favoriteList2.size()).isEqualTo(2);
        result = this.favoriteService.deleteFavorites(favoriteList2);
        assertThat(result).isEqualTo(2);
        List<Favorite> favoriteList3 = this.favoriteService.findFavoritesByUserId(1);
        assertThat(favoriteList3.size()).isEqualTo(0);
    }

    @Test
    public void updateFavorite() {
        Favorite favorite = this.favoriteService.findFavoriteById(1);
        assertThat(favorite).isNotNull();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            favorite.setTime(formatter.parse("2016-12-27 11:11:11"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = this.favoriteService.updateFavorite(favorite);
        assertThat(result).isEqualTo(1);

        Favorite favorite1 = this.favoriteService.findFavoriteById(1);
        assertThat(favorite1).isNotNull();
        try {
            assertThat(favorite1.getTime()).isEqualTo(formatter.parse("2016-12-27 11:11:11"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
