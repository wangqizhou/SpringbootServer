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
import java.util.HashMap;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/9.
 */
@RestController
@RequestMapping("/api/v2")
public class LoginApi {
    private static final int CHALLENGE_LEN = 16;
    private final IUserService userService;
    private final IAuthService desService;
    private final HashMap<String, String> challengeMap;
    private final Logger logger;

    public LoginApi(IUserService userService, @Qualifier("DESService") IAuthService desService) {
        this.userService = userService;
        this.desService = desService;
        this.challengeMap = new HashMap<>();
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/users/login/{username}", method = RequestMethod.GET)
    public String login(@PathVariable(value = "username") String username, HttpServletResponse resp) {
        User user = this.userService.findUserByName(username);
        if (user != null) {
            String challenge = desService.generateRandomString(CHALLENGE_LEN);
            if (challengeMap.containsKey(username)) {
                challengeMap.remove(username);
            }
            challengeMap.put(username, challenge);

            return JSON.toJSONString(challenge);
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND " +
                    "message: " + "not found user with name: " + username);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_FOUND,
                    "not found user with name: " + username);
        }
    }

    @RequestMapping(value = "/users/login/{username}", method = RequestMethod.POST)
    public String challenge(@PathVariable(value = "username") String username,
                            @RequestParam(value = "challenge") String clientChallenge,
                            HttpServletResponse resp) {

        User user = this.userService.findUserByName(username);
        byte[] pwdBytes = desService.decryptFromBase64String(user.getPassword(), desService.getKeyForPassword());

        String challenge = this.challengeMap.get(username);
        SecretKey key = null;
        try {
            key = desService.generateKey(new String(pwdBytes, "UTF-8"));
            String encryptedChallenge = desService.encryptToBase64String(challenge, key);
            if (encryptedChallenge.equals(clientChallenge)) {
                // DO NOT return password to client
                user.setPassword("******");
                return JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss");
            } else {
                logger.error("status:" + HttpServletResponse.SC_UNAUTHORIZED + " UNAUTHORIZED " +
                        "message: " + "wrong password for username: " + username);
                return HttpErrorMessage.build(resp,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "wrong password for username: " + username);
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
