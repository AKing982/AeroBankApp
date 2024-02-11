package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.EmailNotificationDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.entity.NotificationEntity;

import java.util.List;

public interface NotificationService
{
    void createNotification(NotificationEntity notification);
    List<NotificationEntity> getUserNotifications(int userID);
    List<NotificationEntity> findByUserIdAndHasBeenRead(int userId, boolean hasBeenRead);
    void sendEmailNotification(EmailNotificationDTO emailNotification);
    void sendMessageNotification(MessageNotificationDTO messageNotification);
}
