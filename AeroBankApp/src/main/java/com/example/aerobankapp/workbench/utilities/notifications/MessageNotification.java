package com.example.aerobankapp.workbench.utilities.notifications;

import javax.management.Notification;
import java.time.LocalDateTime;

public class MessageNotification extends Notifications
{

    public MessageNotification(String message, String sender, int userID, LocalDateTime time, NotificationType type, boolean hasBeenRead, int priority) {
        super(message, sender, userID, time, type, hasBeenRead, priority);
    }

    @Override
    public void sendNotification() {

    }
}
