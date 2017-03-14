package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Favorite;
import com.evistek.mediaserver.model.FavoriteModel;
import com.evistek.mediaserver.service.IFavoriteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql",
        "classpath:test-users-data.sql",
        "classpath:test-videos-data.sql",
        "classpath:test-favorites-data.sql"})
public class FavoriteApiTests {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private IFavoriteService favoriteService;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getFavoritesByUserId() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/favorites/user_id/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<FavoriteModel> favoriteList = JSON.parseArray(result, FavoriteModel.class);
        assertThat(favoriteList).isNotNull();
        assertThat(favoriteList.size()).isEqualTo(5);
        assertThat(favoriteList.get(0).getVideo().getName()).isEqualTo("柳如是");

        String result1 = this.mockMvc
                .perform(get("/api/v2/favorites/user_id/2")
                        .param("page", "2")
                        .param("page_size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<FavoriteModel> favoriteList1 = JSON.parseArray(result1, FavoriteModel.class);
        assertThat(favoriteList1).isNotNull();
        assertThat(favoriteList1.size()).isEqualTo(2);
        assertThat(favoriteList1.get(0).getVideo().getName()).isEqualTo("黑童话.预告片");

        this.mockMvc
                .perform(get("/api/v2/favorites/user_id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for user_id")));

        this.mockMvc
                .perform(get("/api/v2/favorites/user_id/2")
                        .param("page", "-2")
                        .param("page_size", "-3"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void addFavorite() throws Exception {
        Favorite favorite = new Favorite();
        favorite.setUserId(2);
        favorite.setVideoId(8);
        favorite.setVideoName("少女时代.Genie.mp4");

        String result = this.mockMvc
                .perform(post("/api/v2/favorites")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(favorite)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Favorite favorite1 = JSON.parseObject(result, Favorite.class);
        assertThat(favorite1).isNotNull();
        assertThat(favorite1.getId()).isEqualTo(35);
        assertThat(favorite1.getVideoName()).isEqualTo("少女时代.Genie.mp4");
    }

    @Test
    public void deleteFavorite() throws Exception {
        List<Favorite> favoriteList = this.favoriteService.findFavoritesByUserId(2,
                new OrderBy(IFavoriteService.ORDER_TIME, OrderBy.DESC));
        assertThat(favoriteList.size()).isEqualTo(5);
        assertThat(favoriteList.get(0).getId()).isEqualTo(23);
        assertThat(favoriteList.get(0).getVideoName()).isEqualTo("柳如是");

        this.mockMvc.perform(delete("/api/v2/favorites/id/" + favoriteList.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("success to delete favorite with id: " + favoriteList.get(0).getId())));

        Favorite favorite1 = this.favoriteService.findFavoriteById(favoriteList.get(0).getId());
        assertThat(favorite1).isNull();
    }

    @Test
    public void deleteFavoritesByUserId() throws Exception {
        List<Favorite> favoriteList = this.favoriteService.findFavoritesByUserId(2,
                new OrderBy(IFavoriteService.ORDER_TIME, OrderBy.DESC));
        assertThat(favoriteList.size()).isEqualTo(5);

        this.mockMvc.perform(delete("/api/v2/favorites/user_id/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(
                                "success to delete " + favoriteList.size() + " favorite(s) with user_id: " + 2)));

        List<Favorite> favoriteList1 = this.favoriteService.findFavoritesByUserId(2,
                new OrderBy(IFavoriteService.ORDER_TIME, OrderBy.DESC));
        assertThat(favoriteList1.size()).isEqualTo(0);


        this.mockMvc.perform(delete("/api/v2/favorites/user_id/-2"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .string(containsString("invalid value for user_id")));


        this.mockMvc.perform(delete("/api/v2/favorites/user_id/2"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .string(containsString(
                                "not found favorite with user_id: " + 2)));
    }

    @Test
    public void deleteFavorites() throws Exception {
        Favorite favorite1 = this.favoriteService.findFavoriteById(1);
        Favorite favorite2 = this.favoriteService.findFavoriteById(2);
        Favorite favorite3 = this.favoriteService.findFavoriteById(3);
        assertThat(favorite1.getVideoId()).isEqualTo(13);
        assertThat(favorite2.getVideoId()).isEqualTo(21);
        assertThat(favorite3.getVideoId()).isEqualTo(20);

        List<Favorite> deleteList = new ArrayList<>();
        deleteList.add(favorite1);
        deleteList.add(favorite2);
        deleteList.add(favorite3);

        this.mockMvc.perform(delete("/api/v2/favorites").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(deleteList)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("success to delete " + deleteList.size() + " favorites with " +
                                "userId: " + deleteList.get(0).getUserId())));

        Favorite favorite4 = this.favoriteService.findFavoriteById(deleteList.get(0).getId());
        assertThat(favorite4).isNull();
    }
}
