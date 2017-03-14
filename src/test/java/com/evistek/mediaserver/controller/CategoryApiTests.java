package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Category;
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
@Sql({"classpath:test-schema.sql", "classpath:test-categories-data.sql"})
public class CategoryApiTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getCategories() throws Exception{
        String result = this.mockMvc
                .perform(get("/api/v2/categories"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Category> categoryList = JSON.parseArray(result, Category.class);
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(10);
    }

    @Test
    public void getCategoryById() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/categories/id/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Category category = JSON.parseObject(result, Category.class);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("电影");
        assertThat(category.getType()).isEqualTo("video");

        this.mockMvc
                .perform(get("/api/v2/categories/id/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for id")));
    }

    @Test
    public void getCategoriesByType() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/categories/type/video"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        List<Category> categoryList = JSON.parseArray(result, Category.class);
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(3);
        assertThat(categoryList.get(0).getType()).isEqualTo("video");

        this.mockMvc
                .perform(get("/api/v2/categories/type/application"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for type")));
    }
}
