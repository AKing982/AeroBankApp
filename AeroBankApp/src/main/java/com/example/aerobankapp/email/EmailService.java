package com.example.aerobankapp.email;

public interface EmailService
{
    boolean sendEmail(String toEmail, String fromEmail, String body, String subject);
}
