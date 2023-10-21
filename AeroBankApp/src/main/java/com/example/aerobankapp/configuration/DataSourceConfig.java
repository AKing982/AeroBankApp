package com.example.aerobankapp.configuration;

import com.example.aerobankapp.workbench.utilities.connections.BasicDataSource;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig
{
    private final BasicDataSource dbSource;

    @Autowired
    public DataSourceConfig(BasicDataSourceImpl dataSource)
    {
        this.dbSource = dataSource;
    }

    @Bean
    public DataSource dataSource()
    {
        return DataSourceBuilder
                .create()
                .url(dbSource.getDBURL())
                .username(dbSource.getDBUser())
                .password(dbSource.getDBPass())
                .driverClassName(dbSource.getDBDriver())
                .build();
    }
}
