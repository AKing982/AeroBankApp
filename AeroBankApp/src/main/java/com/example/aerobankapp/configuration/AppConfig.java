package com.example.aerobankapp.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class AppConfig
{
    private DataSource dataSource;

    @Autowired
    public AppConfig(@Qualifier("dataSource") DataSource source)
    {
        this.dataSource = source;
    }

    @Bean
    public String beanString()
    {
        return "";
    }



}
