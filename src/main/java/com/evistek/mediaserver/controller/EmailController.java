package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.config.CustomMailConfig;
import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.service.IEmailService;
import com.evistek.mediaserver.service.impl.EmailGroupServiceImpl;
import com.evistek.mediaserver.utils.OpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/25.
 */
@RestController
public class EmailController {
    private final IEmailService mailService;
    private final EmailGroupServiceImpl emailGroupService;
    private final Logger logger;
    private final OpLogger opLogger;

    public EmailController(OpLogger opLogger, IEmailService mailService, EmailGroupServiceImpl emailGroupService) {
        this.mailService = mailService;
        this.emailGroupService = emailGroupService;
        this.logger = LoggerFactory.getLogger(getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/mail/config", method = RequestMethod.GET)
    public ModelAndView getConfig(HttpServletRequest request) {
        Properties properties = this.mailService.getProperties();
        CustomMailConfig mailConfig = new CustomMailConfig();

        String host = properties.getProperty(IEmailService.PROPERTY_KEY_HOST);
        String protocol = properties.getProperty(IEmailService.PROPERTY_KEY_PROTOCOL);
        int port = Integer.valueOf(properties.getProperty(IEmailService.PROPERTY_KEY_PORT));
        String username = properties.getProperty(IEmailService.PROPERTY_KEY_USERNAME);
        String password = properties.getProperty(IEmailService.PROPERTY_KEY_PASSWORD);

        mailConfig.setHost(host);
        mailConfig.setProtocol(protocol);
        mailConfig.setPort(port);
        mailConfig.setUsername(username);
        mailConfig.setPassword(password);

        ModelAndView modelAndView = new ModelAndView("mail_config");
        modelAndView.addObject("mailConfig", mailConfig);

        this.opLogger.info(request, OpLogger.ACTION_QUERY_EMAIL,
                "query email configuration");

        return modelAndView;
    }

    @RequestMapping(value = "/mail/config", method = RequestMethod.POST)
    public void saveConfig(CustomMailConfig config, HttpServletRequest request) {
        Properties properties = new Properties();
        properties.setProperty(IEmailService.PROPERTY_KEY_HOST, config.getHost());
        properties.setProperty(IEmailService.PROPERTY_KEY_PROTOCOL, config.getProtocol());
        properties.setProperty(IEmailService.PROPERTY_KEY_PORT, String.valueOf(config.getPort()));
        properties.setProperty(IEmailService.PROPERTY_KEY_USERNAME, config.getUsername());
        properties.setProperty(IEmailService.PROPERTY_KEY_PASSWORD, config.getPassword());

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_EMAIL_CONFIG,
                "update email configuration");

        this.mailService.setProperties(properties);
    }

    @RequestMapping(value = "/mail/test", method = RequestMethod.GET)
    public String testConnection() {
        String result = this.mailService.testConnection() ? "success" : "fail";
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/mail/getMembers", method = RequestMethod.GET)
    public String getMembers(HttpServletRequest request){
        List<Admin> adminList = emailGroupService.findMembers();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_EMAIL,
                "query email group members");

        return JSON.toJSONString(adminList);
    }

    @RequestMapping(value = "/mail/addMembers", method = RequestMethod.PUT)
    public String addMember(@RequestBody String json, HttpServletRequest request){
        List<Admin> admins = JSON.parseArray(json, Admin.class);
        List<Admin> list = emailGroupService.findMembers();
        for (Admin admin:list) {
            emailGroupService.deleteMember(admin);
        }
        for (Admin admin:admins) {
            emailGroupService.addMember(admin);
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_EMAIL_GROUP,
                "update email group members");

        return JSON.toJSONString("success");
    }
}
