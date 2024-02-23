package com.example.aerobankapp.email;

import java.util.concurrent.CompletableFuture;

public interface EmailService
{
    CompletableFuture<Boolean> sendEmail(String toEmail, String fromEmail, String body, String subject);
}
