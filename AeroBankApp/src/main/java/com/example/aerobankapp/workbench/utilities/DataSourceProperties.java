package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix="spring.datasource")
public class DataSourceProperties
{
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
