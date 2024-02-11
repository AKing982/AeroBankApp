package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.EmailNotificationDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.entity.NotificationEntity;
import com.example.aerobankapp.repositories.NotificationRepository;
import com.example.aerobankapp.workbench.utilities.notifications.EmailNotificationImpl;
import com.example.aerobankapp.workbench.utilities.notifications.MessageNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService
{
    private EmailNotificationImpl emailNotification;
    private MessageNotification messageNotification;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository)
    {
        this.notificationRepository = notificationRepository;

    }

    @Override
    public void createNotification(NotificationEntity notification) {

    }

    @Override
    public List<NotificationEntity> getUserNotifications(int userID) {
        return null;
    }

    @Override
    public List<NotificationEntity> findByUserIdAndHasBeenRead(int userId, boolean hasBeenRead) {
        return null;
    }

    @Override
    public void sendEmailNotification(EmailNotificationDTO emailNotification)
    {

    }

    @Override
    public void sendMessageNotification(MessageNotificationDTO messageNotification)
    {

    }
}
