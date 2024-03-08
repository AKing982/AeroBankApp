package com.example.aerobankapp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix="db")
public class DatabaseProperties
{
    private String url;
    private String dbPass;
    private String dbUser;
}
