package com.example.aerobankapp.configuration;

import com.example.aerobankapp.email.EmailConfig;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.email.EmailServiceImpl;

public class EmailTestConfig
{
    private static final String MAILHOG_SMTP_HOST = "localhost";
    private static final int MAILHOG_SMTP_PORT = 1025;

    // Method to configure your email service to use MailHog
    public EmailService configureEmailServiceForTesting() {
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setHost(MAILHOG_SMTP_HOST);
        emailConfig.setPort(MAILHOG_SMTP_PORT);
        // other configurations like username, password (if needed)

        return new EmailServiceImpl(emailConfig);
    }
}
