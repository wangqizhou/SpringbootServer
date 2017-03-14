package com.evistek.mediaserver.service;

import java.util.Map;
import java.util.Properties;

/**
 * Created by ymzhao on 2017/1/22.
 */
public interface IEmailService {
    String MSG_SUBJECT = "后台资源上传通知  ";
    String MSG_HEAD = "head";
    String MSG_TYPE = "type";
    String MSG_CATEGORY = "category";
    String MSG_NAME = "name";
    String MSG_FORMAT = "format";
    String MSG_DURATION = "duration";
    String MSG_SIZE = "size";
    String MSG_WIDTH = "width";
    String MSG_HEIGHT = "height";
    String MSG_URL = "url";
    String MSG_THUMBNAIL_URL = "thumbnail_url";
    String MSG_LANDSCAPE_COVER = "landscape_cover";
    String MSG_PORTRAIT_COVER = "portrait_cover";
    String MSG_PRE_IMG1 = "pre_image1";
    String MSG_PRE_IMG2 = "pre_image2";
    String MSG_PRE_IMG3 = "pre_image3";
    String MSG_WEB_URL = "web_url";
    String MSG_UPLOADER = "uploader";
    String MSG_GREETING = "greeting";
    String MSG_GREETING_VALUE = "Hi,All!";
    String MSG_BR = "BR";
    String MSG_COMPANY = "company";
    String MSG_COMPANY_VALUE = "上海易维视科技股份有限公司";
    String MSG_ADDRESS = "address";
    String MSG_ADDRESS_VALUE = "地址:上海市虹口区松花江路2539号1号楼501室，200437";
    String MSG_HEAD_VALUE = "当前后台系统上传的信息如下所示:";
    String PROPERTY_FILE = "mail.properties";
    String PROPERTY_KEY_HOST = "custom.mail.host";
    String PROPERTY_KEY_PROTOCOL = "custom.mail.protocol";
    String PROPERTY_KEY_PORT = "custom.mail.port";
    String PROPERTY_KEY_USERNAME = "custom.mail.username";
    String PROPERTY_KEY_PASSWORD = "custom.mail.password";

    void setProperties(Properties properties);
    Properties getProperties();
    boolean testConnection();
    void sendEmail(String subject, Map<String, String> msg);
}
