package com.evistek.mediaserver.utils;

import com.evistek.mediaserver.service.ILoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/2/27.
 */
@Component
public class OpLogger {
    public static final String ACTION_LOGIN = "LOGIN";
    public static final String ACTION_LOGOUT = "LOGOUT";

    public static final String ACTION_UPLOAD_RESOURCE = "UPLOAD_RESOURCE";
    public static final String ACTION_AUDIT_RESOURCE = "AUDIT_RESOURCE";
    public static final String ACTION_UPDATE_RESOURCE = "UPDATE_RESOURCE";
    public static final String ACTION_DELETE_RESOURCE = "DELETE_RESOURCE";
    public static final String ACTION_UPDATE_DEVICE = "UPDATE_DEVICE";

    public static final String ACTION_BACKUP_DATABASE = "BACKUP_DATABASE";
    public static final String ACTION_RESTORE_DATABASE = "RESTORE_DATABASE";
    public static final String ACTION_DELETE_BACKUP = "DELETE_BACKUP";

    public static final String ACTION_QUERY_USERS = "QUERY_USERS";
    public static final String ACTION_QUERY_ADMINS = "QUERY_ADMINS";
    public static final String ACTION_QUERY_LOGGERS = "QUERY_LOGGERS";
    public static final String ACTION_QUERY_EMAIL = "QUERY_EMAIL";
    public static final String ACTION_QUERY_RESOURCE = "QUERY_RESOURCE";
    public static final String ACTION_QUERY_DOWNLOAD = "QUERY_DOWNLOAD";
    public static final String ACTION_QUERY_DEVICE = "QUERY_DEVICE";
    public static final String ACTION_QUERY_FAVORITE = "QUERY_FAVORITE";
    public static final String ACTION_QUERY_PLAY_RECORD = "QUERY_PLAY_RECORD";
    public static final String ACTION_QUERY_NEW_USER = "QUERY_NEW_USER";
    public static final String ACTION_QUERY_ACTIVE_USER = "QUERY_ACTIVE_USER";
    public static final String ACTION_QUERY_HOT_RESOURCE = "QUERY_HOT_RESOURCE";
    public static final String ACTION_QUERY_DATABASE_BACKUP = "QUERY_DATABASE_BACKUP";

    public static final String ACTION_UPDATE_USER = "UPDATE_USER";
    public static final String ACTION_UPDATE_ADMIN = "UPDATE_ADMIN";
    public static final String ACTION_REGISTER_ADMIN = "REGISTER_ADMIN";

    public static final String ACTION_UPDATE_EMAIL_GROUP = "UPDATE_EMAIL_GROUP";
    public static final String ACTION_UPDATE_EMAIL_CONFIG = "UPDATE_EMAIL_CONFIG";
    private static final int MONTHS = 3;

    private Logger logger;

    private ILoggerService loggerService;

    public OpLogger(ILoggerService loggerService) {
        this.loggerService = loggerService;
        logger = LoggerFactory.getLogger(getClass());
    }

    public void setTag(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void info(HttpServletRequest request, String action, String message) {
        String owner = getOwner(request);
        saveToDatabase(owner, action, message);

        this.logger.info(messageBuilder(owner, action, message));
    }

    public void info(String owner, String action, String message) {
        saveToDatabase(owner, action, message);
        this.logger.info(messageBuilder(owner, action, message));
    }

    public void warn(HttpServletRequest request, String action, String message) {
        String owner = getOwner(request);
        saveToDatabase(owner, action, message);

        this.logger.warn(messageBuilder(owner, action, message));
    }

    private String getOwner(HttpServletRequest request) {
        String owner = "unknown";

        Object attribute = request.getSession().getAttribute("username");
        if (attribute != null) {
            owner = (String) attribute;
        }

        return owner;
    }

    private String messageBuilder(String owner, String action, String message) {
        return "owner: " + owner + " action: " + action + " message: " + message;
    }

    private void saveToDatabase(String owner, String action, String message) {

        com.evistek.mediaserver.entity.Logger logger = new com.evistek.mediaserver.entity.Logger();
        logger.setTime(new Date());
        logger.setAction(action);
        logger.setOwner(owner);
        logger.setMessage(message);
        int result = this.loggerService.addLogger(logger, MONTHS);
        if (result <= 0) {
            this.logger.info("failed save：" + result);
        }
    }
}
