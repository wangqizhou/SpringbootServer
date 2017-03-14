package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.service.IAuthService;
import com.evistek.mediaserver.service.IUserService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/9.
 */
@RestController
@RequestMapping("/api/v2")
public class RegisterApi {
    private static final int CHALLENGE_LEN = 16;
    private final IUserService userService;
    private final IAuthService desService;
    private final HashMap<String, String> registerMap;
    private final Logger logger;

    public RegisterApi(IUserService userService, @Qualifier("DESService") IAuthService desService) {
        this.userService = userService;
        this.desService = desService;
        this.registerMap = new HashMap<>();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public String register(@RequestBody User user, HttpServletResponse resp) {
        if (this.userService.findUserByName(user.getUsername()) != null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid username: " + user.getUsername());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid username: " + user.getUsername());
        } else if (this.userService.findUserByPhone(user.getPhone()) != null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid phone " + user.getPhone() + " for user " + user.getUsername());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid phone " + user.getPhone() + " for user " + user.getUsername());
        } else if (this.userService.findUserByEmail(user.getEmail()) != null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid email " + user.getEmail() + " for user " + user.getUsername());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid email " + user.getEmail() + " for user " + user.getUsername());
        } else {
            String challenge = this.desService.generateRandomString(CHALLENGE_LEN);
            if (this.registerMap.containsKey(user.getUsername())) {
                this.registerMap.remove(user.getUsername());
            }
            this.registerMap.put(user.getUsername(), challenge);

            return JSON.toJSONString(challenge);
        }
    }

    @RequestMapping(value = "/users/register/challenge", method = RequestMethod.POST)
    public String challenge(@RequestBody User user, HttpServletResponse resp) {

        SecretKey key = this.desService.generateKey(this.registerMap.get(user.getUsername()));
        try {
            String password = new String(this.desService.decryptFromBase64String(user.getPassword(), key), "UTF-8");
            String p = this.desService.encryptToBase64String(password, this.desService.getKeyForPassword());

            user.setPassword(p);
            user.setRegisterTime(new Date());
            User addedUser = this.userService.addUser(user);
            if (addedUser != null) {
                // DO NOT return password to client
                addedUser.setPassword("******");
                return JSON.toJSONStringWithDateFormat(addedUser, "yyyy-MM-dd HH:mm:ss");
            } else {
                logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                        "message: " + "fail to register user with name: " + user.getUsername());
                return HttpErrorMessage.build(resp,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "fail to register user with name: " + user.getUsername());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " INTERNAL SERVER ERROR " +
                    "message: " + "internal server error");
            e.printStackTrace();
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "internal server error");
        }
    }
}
