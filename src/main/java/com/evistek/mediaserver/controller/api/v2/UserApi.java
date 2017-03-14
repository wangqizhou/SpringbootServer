package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.User;
import com.evistek.mediaserver.model.UserModel;
import com.evistek.mediaserver.service.IUserService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/2/20.
 */
@RestController
@RequestMapping("/api/v2")
public class UserApi {
    private final IUserService userService;
    private final Logger logger;

    public UserApi(IUserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(value = "/users/name/{name}", method = RequestMethod.GET)
    public String getUserByName(@PathVariable("name") String name, HttpServletResponse resp) {
        if (name == null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE" +
                    "message: " + "user name is null");
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE, "user name is null");
        }

        User user = this.userService.findUserByName(name);
        if (user == null) {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND" +
                    "message: " + "didn't find user with user name: " + name);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_FOUND, "didn't find user with user name: " + name);
        }

        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setUsername(user.getUsername());
        userModel.setNickname(user.getNickname());
        userModel.setPhone(user.getPhone());
        userModel.setEmail(user.getEmail());
        userModel.setSex(user.getSex());
        userModel.setLocation(user.getLocation());
        userModel.setRegisterTime(user.getRegisterTime());
        userModel.setSource(user.getSource());
        userModel.setFigureUrl(user.getFigureUrl());
        userModel.setPhoneDevice(user.getPhoneDevice());
        userModel.setPhoneSystem(user.getPhoneSystem());
        userModel.setVrDevice(user.getVrDevice());

        return JSON.toJSONStringWithDateFormat(userModel, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public String updateUser(@RequestBody User user, HttpServletResponse resp) {

        int result = this.userService.updateUser(user);
        if (result != 1) {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " NOT FOUND" +
                    "message: " + "failed to update user with user name: " + user.getUsername());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST, "failed to update user with user name: " +
                            user.getUsername());
        }

        String msg = "success to update user info with user name: " + user.getUsername();
        return JSON.toJSONString(msg);
    }
}
