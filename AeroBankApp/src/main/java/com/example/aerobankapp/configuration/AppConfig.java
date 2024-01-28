package com.example.aerobankapp.configuration;

import com.example.aerobankapp.repositories.UserRepository;

import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig
{
    private DataSource dataSource;

    @Autowired
    public AppConfig(@Qualifier("dataSource") DataSource source)
    {
        this.dataSource = source;
    }

    @Bean
    public UserService userDAO(UserRepository userRepository, EntityManager entityManager)
    {
        return new UserServiceImpl(userRepository, entityManager);
    }

    @Bean
    public String beanString()
    {
        return "";
    }



}
