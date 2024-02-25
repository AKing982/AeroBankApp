package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.EmailNotificationDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.email.Email;
import com.example.aerobankapp.email.EmailConfig;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.email.EmailServiceImpl;
import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.entity.NotificationEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.NotificationRepository;
import com.example.aerobankapp.workbench.utilities.notifications.EmailNotificationImpl;
import com.example.aerobankapp.workbench.utilities.notifications.MessageNotification;
import com.example.aerobankapp.workbench.utilities.notifications.Notifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
public class NotificationServiceImpl implements NotificationService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final NotificationRepository notificationRepository;

    private final EmailServerService emailServerService;

    private Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    public NotificationServiceImpl(EmailServerServiceImpl emailServer, NotificationRepository notificationRepository, EntityManager entityManager) {
        this.emailServerService = emailServer;
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void createNotification(NotificationEntity notification) {
        getNotificationRepository().save(notification);
    }

    @Override
    public List<NotificationEntity> getUserNotifications(int userID) {
        if (userID < 1) {
            throw new IllegalArgumentException("Invalid UserID found for notifications.");
        }

        EntityManager manager = getEntityManager();
        if (manager == null) {
            throw new NullPointerException("Entity Manager is Null");
        }
        TypedQuery<NotificationEntity> notificationEntityTypedQuery = getEntityManager().createQuery("FROM NotificationEntity WHERE userEntity.userID =:userID", NotificationEntity.class);
        notificationEntityTypedQuery.setParameter("userID", userID);
        notificationEntityTypedQuery.setMaxResults(10);

        return notificationEntityTypedQuery.getResultList();
    }

    @Override
    public List<NotificationEntity> findByUserIdAndHasBeenRead(int userId, boolean hasBeenRead) {
        if (userId < 1) {
            throw new InvalidUserIDException("Caught invalid User ID: " + userId + " unable to proceed...");
        }
        final String userIDAndHasBeenReadQuery = "FROM NotificationEntity WHERE userEntity.userID =:userID AND hasBeenRead =:hasBeenRead";
        TypedQuery<NotificationEntity> notificationEntityTypedQuery = getEntityManager().createQuery(userIDAndHasBeenReadQuery, NotificationEntity.class);
        notificationEntityTypedQuery.setParameter("userID", userId);
        notificationEntityTypedQuery.setParameter("hasBeenRead", hasBeenRead);
        notificationEntityTypedQuery.setMaxResults(10);

        return notificationEntityTypedQuery.getResultList();
    }

    @Override
    public void sendEmailNotification(EmailNotificationDTO emailNotification) {

        // Get the Email Server properties from the database
        List<EmailServerEntity> emailServerList = getEmailServer();
        LOGGER.info("Email Server List Size: " + emailServerList.size());

        EmailServerEntity emailServer = emailServerList.get(0);

        // Create the Mail Server configuration
        EmailConfig emailConfig = buildEmailConfig(emailServer);

        // Get the Email Service implementation
        EmailService emailService = new EmailServiceImpl(emailConfig);

        // Extract the details from the email notification
        String message = emailNotification.message();
        String subject = emailNotification.subject();
        String sender = emailNotification.sender().trim();
        String recipient = emailNotification.recipient().trim();
        int userID = emailNotification.userID();
        LocalDateTime sent = emailNotification.sent();

        // Send the email
        emailService.sendEmail(recipient, sender, message, subject);
    }

    private EmailConfig buildEmailConfig(EmailServerEntity emailServer)
    {
        return EmailConfig.builder()
                .port(emailServer.getPort())
                .host(emailServer.getHost())
                .useSSL(!emailServer.isTLS())
                .useTLS(emailServer.isTLS())
                .username(emailServer.getUsername())
                .password(emailServer.getPassword())
                .build();
    }

    private List<EmailServerEntity> getEmailServer()
    {
        return getEmailServerService().getEmailServerById(1L);
    }

    @Override
    public void sendMessageNotification(MessageNotificationDTO messageNotification)
    {
        if(messageNotification == null){
            throw new IllegalArgumentException("MessageNotificationDTO cannot be null");
        }

        // Extract the Message content
        final String message = messageNotification.message();
        final int userID = messageNotification.userID();
        final String title = messageNotification.title();

        // Build the NotificationEntity
        NotificationEntity notification = NotificationEntity.builder()
                .message(message)
                .userEntity(UserEntity.builder().userID(userID).build())
                .sent(LocalDateTime.now())
                .hasBeenRead(false)
                .build();

        // Save to the database.
        getNotificationRepository().save(notification);
    }

    @Override
    public void markNotificationAsRead(Long notificationID) {

    }

    @Override
    public void deleteNotification(NotificationEntity notification) {
        getNotificationRepository().delete(notification);
    }

    @Override
    public int countUnreadNotifications(int userID) {
        return 0;
    }
}
