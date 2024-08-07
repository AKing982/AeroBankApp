package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.EmailNotificationDTO;
import com.example.aerobankapp.email.EmailConfig;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.email.EmailServiceImpl;
import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.entity.NotificationEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.NotificationRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.notifications.EmailNotificationImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailNotificationImpl emailSender;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EmailServerServiceImpl emailServerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(emailServerService, notificationRepository, entityManager);
    }

    @Test
    public void testCreateNotification() {
        NotificationEntity notification = NotificationEntity.builder()
                .notificationID(1L)
                .message("Deposit has been processed")
                .hasBeenRead(true)
                .priority(1)
                .sent(LocalDateTime.now())
                .userEntity(UserEntity.builder().userID(1).userCredentials(UserCredentials.builder().username("AKing94").build()).userSecurity(UserSecurity.builder().role(Role.ADMIN).build()).build())
                .build();

        notificationService.createNotification(notification);
        verify(notificationRepository).save(any(NotificationEntity.class));
    }

    @Test
    public void testGetUserNotifications_ValidUser() {
        List<NotificationEntity> userNotifications = new ArrayList<>();
        NotificationEntity notification = NotificationEntity.builder()
                .notificationID(1L)
                .sent(LocalDateTime.now())
                .message("Deposit has been processed")
                .hasBeenRead(false)
                .priority(1)
                .userEntity(UserEntity.builder().userID(1).userCredentials(UserCredentials.builder().username("AKing94").build()).userSecurity(UserSecurity.builder().role(Role.ADMIN).build()).build())
                .sent(LocalDateTime.now())
                .build();
        userNotifications.add(notification);

        TypedQuery<NotificationEntity> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(NotificationEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(userNotifications);

        notificationService.createNotification(notification);
        List<NotificationEntity> actualNotifications = notificationService.getUserNotifications(1);

        assertEquals(userNotifications, actualNotifications);
        assertEquals(userNotifications.size(), actualNotifications.size());
        assertEquals(userNotifications.get(0).getNotificationID(), actualNotifications.get(0).getNotificationID());

        verify(notificationRepository).save(notification);
    }


    @Test
    public void testGetUserNotifications_InvalidUser() {
        List<NotificationEntity> userNotifications = new ArrayList<>();
        NotificationEntity notification = NotificationEntity.builder()
                .notificationID(1L)
                .sent(LocalDateTime.now())
                .message("Deposit has been processed")
                .hasBeenRead(false)
                .priority(1)
                .userEntity(UserEntity.builder().userID(1).userCredentials(UserCredentials.builder().username("AKing94").build()).userSecurity(UserSecurity.builder().role(Role.ADMIN).build()).build())
                .sent(LocalDateTime.now())
                .build();
        userNotifications.add(notification);

        TypedQuery<NotificationEntity> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(NotificationEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(userNotifications);

        notificationService.createNotification(notification);

        assertThrows(IllegalArgumentException.class, () -> {
            List<NotificationEntity> actualNotifications = notificationService.getUserNotifications(-1);
        });
        verify(notificationRepository).save(notification);
    }

    @Test
    public void testFindByUserIDAndHasBeenRead_ValidUserID() {
        NotificationEntity mockNotification = createMockNotification(1, "AKing94", "Test Notification", false, 1);
        List<NotificationEntity> expectedNotifications = Collections.singletonList(mockNotification);

        TypedQuery<NotificationEntity> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(NotificationEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedNotifications);

        notificationService.createNotification(mockNotification);

        List<NotificationEntity> notificationsHasBeenRead = notificationService.findByUserIdAndHasBeenRead(1, true);

        assertEquals(expectedNotifications, notificationsHasBeenRead);
        assertEquals(expectedNotifications.size(), notificationsHasBeenRead.size());

    }

    @Test
    public void testSendEmailNotification() {
        // Arrange
        EmailNotificationDTO mockEmailNotification = createMockEmailNotification("Test Email", "Sending Test Email", "alex@utahkings.com", "massgamer1121@gmail.com", 1, 1);
        EmailServerEntity mockEmailServer = EmailServerEntity.builder()
                .id(1L)
                .port(1025)
                .host("localhost")
                .username("sa")
                .password("pass")
                .build();
        List<EmailServerEntity> mockEmailServerList = Arrays.asList(mockEmailServer);
        when(emailServerService.getEmailServerById(1L)).thenReturn(mockEmailServerList);

        // Act
        notificationService.sendEmailNotification(mockEmailNotification);

        // Assert
        // Verifications or assertions can be limited based on the current structure of the code
        // For instance, you can verify that the emailServerService was called
        verify(emailServerService).getEmailServerById(1L);

        // Additional assertions and verifications as per your testing strategy
    }


    private NotificationEntity createMockNotification(int userID, String user, String message, boolean hasBeenRead, int priority)
    {
        return NotificationEntity.builder()
                .notificationID(1L)
                .userEntity(UserEntity.builder().userID(userID).userCredentials(UserCredentials.builder().username(user).build()).userSecurity(UserSecurity.builder().role(Role.ADMIN).build()).build())
                .sent(LocalDateTime.now())
                .message(message)
                .priority(priority)
                .hasBeenRead(hasBeenRead)
                .build();
    }

    private EmailNotificationDTO createMockEmailNotification(String message, String subject, String sender, String recipient, int userID, int priority)
    {
        return EmailNotificationDTO.builder()
                .userID(userID)
                .sender(sender)
                .subject(subject)
                .message(message)
                .priority(priority)
                .recipient(recipient)
                .sent(LocalDateTime.now())
                .build();
    }


    @AfterEach
    void tearDown() {
    }
}