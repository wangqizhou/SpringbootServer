package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
 *
 * Created by ymzhao on 2017/1/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-images-data.sql", "classpath:test-categories-data.sql"})
public class ImageApiTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getImages() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/images"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList = JSON.parseArray(result, Image.class);
        assertThat(imageList).isNotNull();
        assertThat(imageList.size()).isEqualTo(69);

        String result1 = this.mockMvc
                .perform(get("/api/v2/images").param("page", "3").param("page_size", "13"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList1 = JSON.parseArray(result1, Image.class);
        assertThat(imageList1).isNotNull();
        assertThat(imageList1.size()).isEqualTo(13);

        this.mockMvc
                .perform(get("/api/v2/images").param("page", "-3").param("page_size", "-10"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void getImageById() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/images/id/7"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Image image = JSON.parseObject(result, Image.class);
        assertThat(image).isNotNull();
        assertThat(image.getId()).isEqualTo(7);
        assertThat(image.getName()).isEqualTo("f8576c18bc45474a876c8dc0c6dc5d08");

        this.mockMvc
                .perform(get("/api/v2/images/id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for id")));
    }

    @Test
    public void getImagesByCategoryId() throws Exception{
        String result = this.mockMvc
                .perform(get("/api/v2/images/category_id/5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList = JSON.parseArray(result, Image.class);
        assertThat(imageList).isNotNull();
        assertThat(imageList.size()).isEqualTo(8);
        assertThat(imageList.get(0).getName()).isEqualTo("4fbb15879e3f4997bea83e50b1f7ddf7");

        String result1 = this.mockMvc
                .perform(get("/api/v2/images/category_id/4").param("page", "2").param("page_size", "5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList1 = JSON.parseArray(result1, Image.class);
        assertThat(imageList1).isNotNull();
        assertThat(imageList1.size()).isEqualTo(5);
        assertThat(imageList1.get(0).getName()).isEqualTo("89e89087d3d34750b8b841c06be9378d");

        this.mockMvc
                .perform(get("/api/v2/images/category_id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for category_id")));

        this.mockMvc
                .perform(get("/api/v2/images/category_id/4").param("page", "-2").param("page_size", "-5"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void getImagesByCategoryName() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v2/images/category_name/景物");
        request.characterEncoding("utf-8");
        String result = this.mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList = JSON.parseArray(result, Image.class);
        assertThat(imageList).isNotNull();
        assertThat(imageList.size()).isEqualTo(45);
        assertThat(imageList.get(0).getName()).isEqualTo("73a928168a0a4350b19de0f1e04059f4");

        String result1 = this.mockMvc
                .perform(request.param("page", "3").param("page_size", "15"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Image> imageList1 = JSON.parseArray(result1, Image.class);
        assertThat(imageList1).isNotNull();
        assertThat(imageList1.size()).isEqualTo(15);
        assertThat(imageList1.get(0).getName()).isEqualTo("1c0d6741fd784327a289cb6722fc551e");

        this.mockMvc
                .perform(get("/api/v2/images/category_name/scenery"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for category_name")));

        MockHttpServletRequestBuilder request1 = MockMvcRequestBuilders.get("/api/v2/images/category_name/景物");
        request1.characterEncoding("utf-8");
        this.mockMvc
                .perform(request1.param("page", "-2").param("page_size", "-5"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }
}
