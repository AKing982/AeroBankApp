package com.example.aerobankapp.email;

import java.util.concurrent.CompletableFuture;
import org.thymeleaf.context.Context;

public interface EmailService
{
    CompletableFuture<Boolean> sendEmail(String toEmail, String fromEmail, String templateName, Context context, String subject);
}
