package com.example.aerobankapp.configuration;

import com.example.aerobankapp.email.EmailConfig;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.email.EmailServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
@ConfigurationProperties(prefix="mail.smtp")
public class EmailTestConfig
{
    @Bean
    public EmailConfig emailConfig(){
        EmailConfig config = new EmailConfig();
        config.setHost("localhost");
        config.setPort(1025);
        config.setAuthenticationRequired(false);
        config.setUseTLS(false);
        return config;
    }

    @Bean
    public EmailService emailService(EmailConfig config, SpringTemplateEngine templateEngine){
        return new EmailServiceImpl(config, templateEngine);
    }
}
