package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.model.UserModel;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/2/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql"})
public class UserApiTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getUserByName() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/users/name/wxzhang@evistek.com"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        UserModel userModel = JSON.parseObject(result, UserModel.class);

        assertThat(userModel).isNotNull();
        assertThat(userModel.getId()).isEqualTo(2);
        assertThat(userModel.getPhone()).isEqualTo("18616369689");
        assertThat(userModel.getEmail()).isEqualTo("wxzhang@evistek.com");
    }

    @Test
    public void updateUser() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setId(2);
        userModel.setUsername("wxzhang@evistek.com");
        userModel.setNickname("zwx");

        this.mockMvc
                .perform(put("/api/v2/users")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(userModel)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("success to update user info with user name")));
    }
}
