package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.service.IAuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql"})
public class LoginApiTests {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    @Qualifier("DESService")
    private IAuthService desService;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void login() throws Exception{
        String result = this.mockMvc
                .perform(get("/api/v2/users/login/wxzhang@evistek.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).isNotNull();

        // 使用密码对challenge字符串进行加密
        SecretKey key = desService.generateKey("evistek");
        String encryptedChallenge = desService.encryptToBase64String(JSON.parseObject(result, String.class), key);

        String result1 = this.mockMvc
                .perform(post("/api/v2/users/login/wxzhang@evistek.com")
                        .param("challenge",encryptedChallenge))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result1).isNotNull();

        User user = JSON.parseObject(result1, User.class);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("wxzhang@evistek.com");
        assertThat(user.getPassword()).isEqualTo("******");
    }


    @Test
    public void loginFail() throws Exception{
        String result = this.mockMvc
                .perform(get("/api/v2/users/login/wxzhang@evistek.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).isNotNull();

        // 使用密码对challenge字符串进行加密
        String wrongPassword = "evistek817";
        SecretKey key = desService.generateKey(wrongPassword);
        String encryptedChallenge = desService.encryptToBase64String(JSON.parseObject(result, String.class), key);

        this.mockMvc
                .perform(post("/api/v2/users/login/wxzhang@evistek.com")
                        .param("challenge",encryptedChallenge))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("wrong password for username:")));


        String wrongUsername = "wxhang@evistek.com";
        this.mockMvc
                .perform(get("/api/v2/users/login/" + wrongUsername))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found user with name: " + wrongUsername)));
    }

}
