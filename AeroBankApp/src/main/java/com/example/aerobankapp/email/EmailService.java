package com.example.aerobankapp.email;

public interface EmailService
{
    void sendEmail(String toEmail, String fromEmail, String body, String subject);
}
