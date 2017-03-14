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
import org.springframework.http.MediaType;
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
public class RegisterApiTests {
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
    public void register() throws Exception {
        User user = new User();
        user.setUsername("newUser");

        String result = this.mockMvc
                .perform(post("/api/v2/users/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).isNotNull();

        // 使用challenge对密码字符串进行加密
        String challenge = JSON.parseObject(result, String.class);
        SecretKey key = desService.generateKey(challenge);
        String encryptedPwd = this.desService.encryptToBase64String("evistek817", key);

        user.setPassword(encryptedPwd);
        user.setNickname("newNickname");
        user.setEmail("newuser@evistek.com");
        user.setPhone("18616369689");
        user.setPhone("K550");
        user.setPhoneSystem("Android 5.1");
        user.setSource("evistek");
        user.setSex("男");
        user.setLocation("上海 虹口区");

        String result1 = this.mockMvc
                .perform(post("/api/v2/users/register/challenge")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result1).isNotNull();

        User addedUser = JSON.parseObject(result1, User.class);
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isEqualTo(17);
        assertThat(addedUser.getUsername()).isEqualTo("newUser");
        assertThat(addedUser.getPassword()).isEqualTo("******");
    }

    @Test
    public void registerFail() throws Exception {
        User user = new User();
        user.setUsername("wxzhang@evistek.com");
        this.mockMvc
                .perform(post("/api/v2/users/register")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(user)))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string(containsString("invalid username: " + user.getUsername())));

        user.setUsername("newUser");
        user.setPhone("18616369689");
        this.mockMvc
                .perform(post("/api/v2/users/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(user)))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(content()
                        .string(containsString("invalid phone " + user.getPhone() +
                                " for user " + user.getUsername())));

        user.setUsername("newUser");
        user.setPhone("18612345678");
        user.setEmail("wxzhang@evistek.com");
        this.mockMvc
                .perform(post("/api/v2/users/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(user)))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(content()
                        .string(containsString("invalid email " + user.getEmail() +
                                " for user " + user.getUsername())));
    }
}
