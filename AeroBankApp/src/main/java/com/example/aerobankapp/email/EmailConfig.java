package com.example.aerobankapp.email;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="mail.smtp")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfig
{

    private String host;

    private int port;

    private String username;
    private String password;
    private boolean useTLS;
    private boolean useSSL;
    private boolean isAuthenticationRequired;
}
