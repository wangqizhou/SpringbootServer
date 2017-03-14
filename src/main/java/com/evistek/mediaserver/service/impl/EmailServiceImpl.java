package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.config.CustomMailConfig;
import com.evistek.mediaserver.service.IEmailGroupService;
import com.evistek.mediaserver.service.IEmailService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by ymzhao on 2017/1/22.
 */
@Service
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSenderImpl mailSender;
    private final IEmailGroupService mailGroupService;
    private final CustomMailConfig mailConfig;
    private SpringTemplateEngine templateEngine;
    private Context ctx;

    public EmailServiceImpl(JavaMailSenderImpl mailSender, IEmailGroupService mailGroupService,
                            CustomMailConfig emailConfig, SpringTemplateEngine templateEngine
                            ) {
        this.mailSender = mailSender;
        this.mailGroupService = mailGroupService;
        this.mailConfig = emailConfig;
        this.templateEngine = templateEngine;

        /**
         * 默认使用application.properties中的邮件配置
         * 如果, mail.properties中有自定义配置，则覆盖application.properties中对应的配置
         *
         * */
        if (this.mailConfig.getHost() != null) {
            this.mailSender.setHost(this.mailConfig.getHost());
        }

        if (this.mailConfig.getProtocol() != null) {
            this.mailSender.setProtocol(this.mailConfig.getProtocol());
        }

        if (this.mailConfig.getPort() != 0) {
            this.mailSender.setPort(this.mailConfig.getPort());
        }

        if (this.mailConfig.getUsername() != null) {
            this.mailSender.setUsername(this.mailConfig.getUsername());
        }

        if (this.mailConfig.getPassword() != null) {
            this.mailSender.setPassword(this.mailConfig.getPassword());
        }
    }

    @Override
    public void setProperties(Properties properties) {
        String host = properties.getProperty(PROPERTY_KEY_HOST);
        if (host != null) {
            this.mailSender.setHost(host);
        }

        String protocol = properties.getProperty(PROPERTY_KEY_PROTOCOL);
        if (protocol != null) {
            this.mailSender.setProtocol(protocol);
        }

        int port = Integer.valueOf(properties.getProperty(PROPERTY_KEY_PORT));
        if (port > 0) {
            this.mailSender.setPort(port);
        }

        String username = properties.getProperty(PROPERTY_KEY_USERNAME);
        if (username != null) {
            this.mailSender.setUsername(username);
        }

        String password = properties.getProperty(PROPERTY_KEY_PASSWORD);
        if (password != null) {
            this.mailSender.setPassword(password);
        }

        ClassPathResource classPathResource = new ClassPathResource(PROPERTY_FILE);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(classPathResource.getFile());
            properties.store(fos, "Custom configuration for mail" +
                    " which will override values in application.properties.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_KEY_HOST, this.mailSender.getHost());
        properties.setProperty(PROPERTY_KEY_PROTOCOL, this.mailSender.getProtocol());
        properties.setProperty(PROPERTY_KEY_PORT, String.valueOf(this.mailSender.getPort()));
        properties.setProperty(PROPERTY_KEY_USERNAME, this.mailSender.getUsername());
        properties.setProperty(PROPERTY_KEY_PASSWORD, this.mailSender.getPassword());

        return properties;
    }

    @Override
    public boolean testConnection() {
        try {
            this.mailSender.testConnection();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sendEmail(String subject, Map<String, String> msg) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(this.mailSender.getUsername());

            List<String> mailList = this.mailGroupService.findEmails();
            String[] to = new String[mailList.size()];
            this.mailGroupService.findEmails().toArray(to);
            helper.setTo(to);
            helper.setSubject(MSG_SUBJECT + subject + " " + new Date());
            ctx = new Context();
            ctx.setVariable(MSG_HEAD, MSG_HEAD_VALUE);
            ctx.setVariable(MSG_TYPE, msg.get(MSG_TYPE));
            ctx.setVariable(MSG_CATEGORY, msg.get(MSG_CATEGORY));
            ctx.setVariable(MSG_NAME, msg.get(MSG_NAME));
            ctx.setVariable(MSG_FORMAT, msg.get(MSG_FORMAT));
            ctx.setVariable(MSG_DURATION, msg.get(MSG_DURATION));
            ctx.setVariable(MSG_SIZE, msg.get(MSG_SIZE));
            ctx.setVariable(MSG_WIDTH, msg.get(MSG_WIDTH));
            ctx.setVariable(MSG_HEIGHT, msg.get(MSG_HEIGHT));
            ctx.setVariable(MSG_URL, msg.get(MSG_URL));
            ctx.setVariable(MSG_LANDSCAPE_COVER, msg.get(MSG_LANDSCAPE_COVER));
            ctx.setVariable(MSG_PORTRAIT_COVER, msg.get(MSG_PORTRAIT_COVER));
            ctx.setVariable(MSG_PRE_IMG1, msg.get(MSG_PRE_IMG1));
            ctx.setVariable(MSG_PRE_IMG2, msg.get(MSG_PRE_IMG2));
            ctx.setVariable(MSG_PRE_IMG3, msg.get(MSG_PRE_IMG3));
            ctx.setVariable(MSG_UPLOADER, msg.get(MSG_UPLOADER));
            ctx.setVariable(MSG_WEB_URL, msg.get(MSG_WEB_URL));
            ctx.setVariable(MSG_THUMBNAIL_URL, msg.get(MSG_THUMBNAIL_URL));
            ctx.setVariable(MSG_GREETING, MSG_GREETING_VALUE);
            ctx.setVariable(MSG_BR, MSG_BR);
            ctx.setVariable(MSG_COMPANY, MSG_COMPANY_VALUE);
            ctx.setVariable(MSG_ADDRESS, MSG_ADDRESS_VALUE);
            String str = templateEngine.process("mail", ctx);
            helper.setText(str,true);
            ClassPathResource image = new ClassPathResource(buildImagePath());
            helper.addInline("mail_logo", image);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public String buildImagePath() {
        return new StringBuilder()
                .append("static").append(File.separator)
                .append("images").append(File.separator)
                .append("mail_logo.jpg")
                .toString();
    }
}
