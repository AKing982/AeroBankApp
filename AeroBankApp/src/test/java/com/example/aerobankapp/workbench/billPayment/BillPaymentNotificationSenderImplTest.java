package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BillPaymentNotificationSenderImplTest {

    @Autowired
    private BillPaymentNotificationSender billPaymentNotificationSender;

    private final String PAYEE_NAME = "Payee Test";
    private final ScheduleFrequency MONTHLY = ScheduleFrequency.MONTHLY;
    private final AccountCode ACCOUNT_CODE = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);


    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
    }
}