package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidAccountNotificationException;
import com.example.aerobankapp.exceptions.InvalidLatePaymentException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
class LatePaymentNotificationSenderTest {

    @InjectMocks
    private LatePaymentNotificationSender notificationSender;

    @Mock
    private AccountNotificationService accountNotificationService;

    @Mock
    private EntityBuilder<AccountNotificationEntity, AccountNotification> entityAccountNotificationEntityBuilder;

    @Mock
    private NotificationStrategy<LateBillPayment> lateBillPaymentNotification;


    @BeforeEach
    void setUp() {
        notificationSender = new LatePaymentNotificationSender(accountNotificationService, entityAccountNotificationEntityBuilder);
    }

    @Test
    @DisplayName("Test create Message with null late payment then throw exception")
    public void testCreateMessageWithNull_LatePayment_thenThrowException() {
        assertThrows(InvalidLatePaymentException.class, () -> notificationSender.createMessage(null));

    }

    @Test
    @DisplayName("Test create Message with valid late payment, then return message")
    public void testCreateMessageWithValid_LatePayment_thenReturnMessage() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 3))
                .dueDate(LocalDate.of(2024, 6, 1))
                .userID(1)
                .paymentType("ACCOUNT")
                .paymentAmount(new BigDecimal("100.00"))
                .scheduleStatus(ScheduleStatus.PENDING)
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .payeeName("Payee Test")
                .isAutoPayEnabled(true)
                .accountCode(accountCode)
                .posted(LocalDate.now())
                .build();

        LateBillPayment expected = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        StringBuilder message = new StringBuilder("Your payment of $100.00 was due on 2024-06-01. As of 2024-06-07, your payment is 2 days late. Late fee incurred: $25.00.");
        StringBuilder actualMessage = notificationSender.createMessage(expected);

        assertEquals(message.toString(), actualMessage.toString());
        assertNotNull(actualMessage);
    }

    @Test
    @DisplayName("Test send with null Account Notification, then throw exception")
    public void testSendWithNullAccountNotification_thenThrowException() {
        assertThrows(InvalidAccountNotificationException.class, () -> notificationSender.send(null));
    }

    @Test
    @DisplayName("Test send with Account Notification with valid parameters, then return true")
    public void testSendWithAccountNotification_withValidParameters_thenReturnTrue(){
        StringBuilder testMessage = new StringBuilder("This is a test");
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 3))
                .dueDate(LocalDate.of(2024, 6, 1))
                .userID(1)
                .paymentType("ACCOUNT")
                .paymentAmount(new BigDecimal("100.00"))
                .scheduleStatus(ScheduleStatus.PENDING)
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .payeeName("Payee Test")
                .isAutoPayEnabled(true)
                .accountCode(accountCode)
                .posted(LocalDate.now())
                .build();

        LateBillPayment expected = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        AccountNotification accountNotification = AccountNotificationUtil.buildAccountNotificationModel(testMessage, expected);

        boolean result = notificationSender.send(accountNotification);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test send with null Account Notification parameters, then throw exception")
    public void testSendWithNullParameters_thenThrowException() {

    }


    @AfterEach
    void tearDown() {
    }
}