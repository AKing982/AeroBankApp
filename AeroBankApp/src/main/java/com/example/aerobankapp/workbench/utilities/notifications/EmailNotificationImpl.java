package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.email.Email;
import com.example.aerobankapp.email.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public class EmailNotificationImpl extends Notifications
{
    private Email email;
    private EmailService emailSender;

    public EmailNotificationImpl(EmailService emailService, String subject, String message, String sender, String toEmail, int userID, LocalDateTime time, NotificationType type, boolean hasBeenRead, int priority) {
        super(message, userID, time, type, hasBeenRead, priority);
        this.email = buildEmail(subject, message, sender, toEmail, time);
        this.emailSender = emailService;
    }

    public Email buildEmail(String subject, String message, String sender, String toEmail, LocalDateTime time)
    {
        return Email.builder()
                .senderEmail(sender)
                .timeStamp(time)
                .subject(subject)
                .toEmail(toEmail)
                .body(message)
                .build();
    }

    @Override
    public void sendNotification()
    {
      //  emailSender.sendEmail(email.getToEmail(), email.getSenderEmail(), email.getSubject(), email.getBody());
    }
}