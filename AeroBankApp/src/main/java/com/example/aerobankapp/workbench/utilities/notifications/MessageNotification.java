package com.example.aerobankapp.workbench.utilities.notifications;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.time.LocalDateTime;


@AllArgsConstructor
public class MessageNotification extends Notifications
{

    public MessageNotification(String message, String sender, int userID, LocalDateTime time, NotificationType type, boolean hasBeenRead, int priority) {
        super(message, userID, time, type, hasBeenRead, priority);
    }

    @Override
    public void sendNotification() {

    }
}
