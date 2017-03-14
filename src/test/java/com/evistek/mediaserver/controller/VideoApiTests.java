package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Video;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@Sql({"classpath:test-schema.sql", "classpath:test-videos-data.sql", "classpath:test-categories-data.sql"})
public class VideoApiTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getVideos() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/videos"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList = JSON.parseArray(result, Video.class);
        assertThat(videoList).isNotNull();
        assertThat(videoList.size()).isEqualTo(25);

        String result1 = this.mockMvc
                .perform(get("/api/v2/videos").param("page", "3").param("page_size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList1 = JSON.parseArray(result1, Video.class);
        assertThat(videoList1).isNotNull();
        assertThat(videoList1.size()).isEqualTo(5);

        this.mockMvc
                .perform(get("/api/v2/videos").param("page", "-3").param("page_size", "-10"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void getVideoById() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/videos/id/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Video video = JSON.parseObject(result, Video.class);
        assertThat(video).isNotNull();
        assertThat(video.getId()).isEqualTo(1);
        assertThat(video.getName()).isEqualTo("冰川时代4：大陆漂移");

        this.mockMvc
                .perform(get("/api/v2/videos/id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for id")));
    }

    @Test
    public void getVideosByCategoryId() throws Exception{
        String result = this.mockMvc
                .perform(get("/api/v2/videos/category_id/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList = JSON.parseArray(result, Video.class);
        assertThat(videoList).isNotNull();
        assertThat(videoList.size()).isEqualTo(9);
        assertThat(videoList.get(0).getName()).isEqualTo("美国队长3预告");

        String result1 = this.mockMvc
                .perform(get("/api/v2/videos/category_id/1").param("page", "2").param("page_size", "5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList1 = JSON.parseArray(result1, Video.class);
        assertThat(videoList1).isNotNull();
        assertThat(videoList1.size()).isEqualTo(4);
        assertThat(videoList1.get(0).getName()).isEqualTo("地心引力");

        this.mockMvc
                .perform(get("/api/v2/videos/category_id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for category_id")));

        this.mockMvc
                .perform(get("/api/v2/videos/category_id/1").param("page", "-2").param("page_size", "-5"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void getVideosByCategoryName() throws Exception{
        //TODO 中文路径会乱码
        String result = this.mockMvc
                .perform(get("/api/v2/videos/category_name/MV"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList = JSON.parseArray(result, Video.class);
        assertThat(videoList).isNotNull();
        assertThat(videoList.size()).isEqualTo(3);
        assertThat(videoList.get(0).getName()).isEqualTo("少女时代.Genie.mp4");

        String result1 = this.mockMvc
                .perform(get("/api/v2/videos/category_name/MV").param("page", "2").param("page_size", "2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Video> videoList1 = JSON.parseArray(result1, Video.class);
        assertThat(videoList1).isNotNull();
        assertThat(videoList1.size()).isEqualTo(1);
        assertThat(videoList1.get(0).getName()).isEqualTo("黑眼豆豆演唱会.mp4");

        this.mockMvc
                .perform(get("/api/v2/videos/category_name/history"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for category_name")));

        this.mockMvc
                .perform(get("/api/v2/videos/category_name/MV").param("page", "-2").param("page_size", "-5"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

}
