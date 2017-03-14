package com.evistek.mediaserver.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/21.
 */
@Configuration
public class TestDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.test")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
