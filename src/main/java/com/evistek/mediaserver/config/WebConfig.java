package com.evistek.mediaserver.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/3.
 */
@Component
public class WebConfig extends WebMvcConfigurerAdapter{
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /*
            如果不关闭，路径参数句号"."及其后面的部分会被截断。

            比如，client端的URL为/db/restore/2017-01-03-04-06-46.sql
            在Controller只能获取到2017-01-03-04-06-46
         */
        configurer.setUseSuffixPatternMatch(false);
    }
}
