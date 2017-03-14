package com.evistek.mediaserver.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/3.
 */
@Component
public class CustomJacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
