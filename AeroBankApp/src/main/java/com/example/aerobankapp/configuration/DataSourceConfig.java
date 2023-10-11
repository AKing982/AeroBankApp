package com.example.aerobankapp.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig
{
    public DataSourceConfig()
    {

    }

    @Bean
    public DataSource dataSource()
    {
        return DataSourceBuilder
                .create()
                .url("")
                .username("")
                .password("")
                .driverClassName("")
                .build();
    }
}
