package com.nsc.dataconfig;

import com.nsc.Amoski.entity.IdWorker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author 李阳
 * @date 2019/12/19 15:56
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }


    @Primary
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDateSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "activityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.activity")
    public DataSource activityDateSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userJdbcTemplate")
    public JdbcTemplate userJdbcTemplate(@Qualifier(value = "userDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "activityJdbcTemplate")
    public JdbcTemplate fileJdbcTemplate(@Qualifier(value = "activityDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}