package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.AccountNotificationEntityBuilderImpl;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentNotificationSenderImplTest {

    @InjectMocks
    private BillPaymentNotificationSenderImpl billPaymentNotificationSender;

    @Mock
    private AccountNotificationService accountNotificationService;

    @Mock
    private NotificationMessageBuilder notificationMessageBuilder;;

    @Mock
    private EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder;

    private final String PAYEE_NAME = "Payee Test";
    private final ScheduleFrequency MONTHLY = ScheduleFrequency.MONTHLY;
    private final AccountCode ACCOUNT_CODE = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
    private AccountNotificationEntityBuilderImpl accountNotificationEntityBuilderImpl;


    @BeforeEach
    void setUp() {
        billPaymentNotificationSender = new BillPaymentNotificationSenderImpl(accountNotificationService, notificationMessageBuilder, accountNotificationEntityBuilder);
        accountNotificationEntityBuilderImpl = new AccountNotificationEntityBuilderImpl();
        mockStatic(AccountNotificationUtil.class);
    }

    @Test
    @DisplayName("Test sendPaymentNotification with null payment, then throw exception")
    public void testSendPaymentNotificationWithNullPayment_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> billPaymentNotificationSender.sendPaymentNotification(null));
    }

    @Test
    @DisplayName("Test sendPaymentNotification with valid payment, then return true")
    public void testSendPaymentNotificationWithValidPayment_thenReturnTrue() {
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("50.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 24))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        boolean result = billPaymentNotificationSender.sendPaymentNotification(billPayment);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test sendProcessedPaymentNotification with null payment, then throw exception")
    public void testSendProcessedPaymentNotificationWithNullPayment_thenThrowException() {
        assertThrows(InvalidProcessedBillPaymentException.class, () -> billPaymentNotificationSender.sendProcessedPaymentNotification(null));
    }

    @Test
    @DisplayName("Test sendProcessedPaymentNotification with valid payment, then return true")
    public void testSendProcessedPaymentNotificationWithValidPayment_thenReturnTrue() {
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("50.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 24))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        ProcessedBillPayment processedBillPayment = new ProcessedBillPayment(billPayment, true, billPayment.getScheduledPaymentDate(), LocalDate.of(2024, 7, 20));

        boolean result = billPaymentNotificationSender.sendProcessedPaymentNotification(processedBillPayment);
        assertTrue(result);
    }


    @Test
    @DisplayName("Test sendProcessedPaymentNotification when PaymentMessage is null, then return false")
    public void testSendProcessedPaymentNotification_whenPaymentMessageNull_returnFalse(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 19))
                .dueDate(null)
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();
        ProcessedBillPayment payment = new ProcessedBillPayment(billPayment, true, LocalDate.now(), LocalDate.of(2024, 6, 19));

        when(notificationMessageBuilder.buildProcessedPaymentNotification(payment)).thenReturn(null);

        boolean result = billPaymentNotificationSender.sendProcessedPaymentNotification(payment);
        assertFalse(result);
        verify(accountNotificationService, never()).save(any(AccountNotificationEntity.class));
    }

    @Test
    @DisplayName("Test sendProcessedPaymentNotification when AccountNotification is null, then return false")
    public void testSendProcessedPaymentNotification_whenAccountNotificationEntityNull_returnFalse(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 19))
                .dueDate(null)
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();
        ProcessedBillPayment payment = new ProcessedBillPayment(billPayment, true, LocalDate.now(), LocalDate.of(2024, 6, 19));

        AccountNotification mockAccountNotification = mock(AccountNotification.class);
        when(accountNotificationEntityBuilderImpl.createEntity(mockAccountNotification)).thenReturn(null);

        boolean result = billPaymentNotificationSender.sendProcessedPaymentNotification(payment);
        assertFalse(result);
        verify(accountNotificationService, never()).save(any(AccountNotificationEntity.class));
    }

    @Test
    @DisplayName("Test sendProcessedPaymentNotification when account notification model null, then throw exception")
    public void testSendProcessedPaymentNotification_whenAccountNotificationModelNull_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 19))
                .dueDate(null)
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();
        ProcessedBillPayment payment = new ProcessedBillPayment(billPayment, true, LocalDate.now(), LocalDate.of(2024, 6, 19));
        StringBuilder mockMessage = new StringBuilder("Test message");

        when(notificationMessageBuilder.buildProcessedPaymentNotification(payment)).thenReturn(mockMessage);
        when(AccountNotificationUtil.buildAccountNotificationModel(mockMessage, payment)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            billPaymentNotificationSender.sendProcessedPaymentNotification(payment);
        });
        verify(accountNotificationService, never()).save(any(AccountNotificationEntity.class));
    }


    @AfterEach
    void tearDown() {
    }
}