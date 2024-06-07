package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.exceptions.IllegalDateException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidLatePaymentException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import com.example.aerobankapp.workbench.utilities.notifications.LatePaymentMessageStrategy;
import com.example.aerobankapp.workbench.utilities.notifications.LatePaymentNotificationSender;
import com.example.aerobankapp.workbench.utilities.notifications.NotificationStrategy;
import com.example.aerobankapp.workbench.utilities.notifications.SystemNotificationSender;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class LatePaymentProcessorTest {

    @InjectMocks
    private LatePaymentProcessor processor;

    @Mock
    private LatePaymentNotificationSender notificationSender;

    @BeforeEach
    void setUp() {
        processor = new LatePaymentProcessor(notificationSender);
    }

    @Test
    @DisplayName("Process Late Payment when Bill Payment is null, then throw exception")
    public void processLatePayment_whenBillPaymentIsNull_thenThrowException() {
        assertThrows(InvalidBillPaymentException.class, () -> {
            processor.processLatePayment(null);
        });
    }

    @Test
    @DisplayName("Test process Late Payment when due date is missing, then throw exception")
    public void processLatePayment_whenDueDateIsNull_thenThrowException() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 5, 29))
                .dueDate(null)
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

        assertThrows(IllegalDateException.class, () -> {
            processor.processLatePayment(billPayment);
        });
    }

    @Test
    @DisplayName("Test Process Late Payment when payment amount is null, then throw exception")
    public void processLatePayment_whenPaymentAmountIsNull_thenThrowException() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 5, 29))
                .dueDate(LocalDate.of(2024, 6,1))
                .userID(1)
                .paymentType("ACCOUNT")
                .paymentAmount(null)
                .scheduleStatus(ScheduleStatus.PENDING)
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .payeeName("Payee Test")
                .isAutoPayEnabled(true)
                .accountCode(accountCode)
                .posted(LocalDate.now())
                .build();

        assertThrows(InvalidBillPaymentException.class, () -> {
            processor.processLatePayment(billPayment);
        });
    }

    @Test
    @DisplayName("Test Process Late Payment when Payment Valid, return Processed Bill Payment")
    public void testProcessLatePayment_whenPaymentValid_thenReturnProcessedBillPayment() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 5, 29))
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

        ProcessedBillPayment expectedProcessedBillPayment = new ProcessedBillPayment();

        ProcessedBillPayment processedBillPayment = processor.processLatePayment(billPayment);

        assertNotNull(processedBillPayment);
        assertEquals(expectedProcessedBillPayment, processedBillPayment);
    }

    @Test
    @DisplayName("Test is Bill Payment Late when payment is null, then return false")
    public void testIsBillPaymentLate_whenPaymentIsNull_thenThrowException() {
        BillPayment billPayment = null;
        assertThrows(InvalidBillPaymentException.class, () -> {
            processor.isBillPaymentLate(billPayment);
        });
    }

    @Test
    @DisplayName("Test Bill Payment is Late when payment is valid and payment date is after due date, then return true")
    public void testIsBillPaymentLate_whenPaymentIsValid_thenReturnTrue() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 5, 29))
                .dueDate(LocalDate.of(2024, 5, 28))
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

        boolean isLate = processor.isBillPaymentLate(billPayment);

        assertTrue(isLate);
    }

    @Test
    @DisplayName("Test Is Bill Payment late when payment is valid, payment date is before due date, return false")
    public void testIsBillPaymentLate_whenPaymentIsValid_thenReturnFalse() {
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 5, 29))
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

        boolean result = processor.isBillPaymentLate(billPayment);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test Calculate Total Amount Due when payment is 1 day late, then return $25.00 late fee")
    public void testCalculateTotalAmountDue_whenPaymentIsOneDayLate_thenReturnTwentFiveLateFee(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 2))
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

        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        BigDecimal expected = new BigDecimal("125.00");

        BigDecimal actual = processor.calculateTotalAmountDue(lateBillPayment);
        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test Calculate Total Amount Due when payment is two days late, then return payment and late fee")
    public void testCalculateTotalAmountDue_whenPaymentIsTwoDaysLate_thenReturnTwentFiveLateFee(){
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

        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        BigDecimal expected = new BigDecimal("130.00");

        BigDecimal actual = processor.calculateTotalAmountDue(lateBillPayment);
        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test calculateTotalAmountDue when payment is 7 day")
    public void testCalculateTotalAmountDue_whenPaymentAmountIsSevenDaysLate_returnPaymentWithLateFee(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 5))
                .dueDate(LocalDate.of(2024, 5, 29))
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

        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        BigDecimal expected = new BigDecimal("170.00");

        BigDecimal actual = processor.calculateTotalAmountDue(lateBillPayment);

        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test CalculateTotalAmountDue when payment is 2 weeks past due date, then return payment with late fee")
    public void testCalculateTotalAmountDue_whenPaymentIsTwoWeeksPastDueDate_thenReturnLateFee(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 12))
                .dueDate(LocalDate.of(2024, 5, 29))
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
        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        BigDecimal expected = new BigDecimal("220.00");
        BigDecimal actual = processor.calculateTotalAmountDue(lateBillPayment);

        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test Send LatePayment Notification when late payment is null then throw exception")
    public void testSendLatePaymentNotification_whenLatePaymentIsNull_thenThrowException(){
        assertThrows(InvalidLatePaymentException.class, () -> {
            processor.sendLatePaymentNotification(null);
        });
    }

    @Test
    @DisplayName("Test Send LatePayment Notification when late payment criteria is null then throw exception")
    public void testSendLatePaymentNotification_whenLatePaymentCriteriaIsNull_thenThrowException(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(null)
                .dueDate(null)
                .userID(1)
                .paymentType("ACCOUNT")
                .paymentAmount(null)
                .scheduleStatus(ScheduleStatus.PENDING)
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .payeeName(null)
                .isAutoPayEnabled(true)
                .accountCode(null)
                .posted(LocalDate.now())
                .build();
        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);

        assertThrows(InvalidLatePaymentException.class, () -> {
            processor.sendLatePaymentNotification(lateBillPayment);
        });
    }

    @Test
    @DisplayName("Test Send Late Payment Notification when late payment is valid return true")
    public void testSendLatePaymentNotification_whenLatePaymentIsValidReturnTrue_thenReturnTrue(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(LocalDate.of(2024, 6, 5))
                .dueDate(LocalDate.of(2024, 5, 29))
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


        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);
        StringBuilder message = new StringBuilder("This is a test");
        AccountNotification mockAccountNotification = AccountNotificationUtil.buildAccountNotificationModel(message, lateBillPayment);
        LatePaymentNotificationSender mockLatePaymentNotificationSender = mock(LatePaymentNotificationSender.class);
        when(mockLatePaymentNotificationSender.send(any(AccountNotification.class))).thenReturn(true);
        //Inject mocked LatePaymentNotificationSender into processor
        ReflectionTestUtils.setField(processor, "latePaymentNotificationSender", mockLatePaymentNotificationSender);


        boolean actual = processor.sendLatePaymentNotification(lateBillPayment);
        assertTrue(actual);
    }

    @Test
    @DisplayName("Test Process Single Payment when late payment is null then throw exception")
    public void testProcessSinglePayment_whenLatePaymentIsNull_thenThrowException(){
        assertThrows(InvalidLatePaymentException.class, () -> {
            processor.processSinglePayment(null);
        });
    }

    @Test
    @DisplayName("Test Process Single Payment when late payment criteria is null then throw exception")
    public void testProcessSinglePayment_whenLatePaymentCriteriaIsNull_thenThrowException(){
        AccountCode accountCode = mock(AccountCode.class);
        BillPayment billPayment = BillPayment.builder()
                .scheduledPaymentDate(null)
                .dueDate(null)
                .userID(1)
                .paymentType("ACCOUNT")
                .paymentAmount(null)
                .scheduleStatus(ScheduleStatus.PENDING)
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .payeeName(null)
                .isAutoPayEnabled(true)
                .accountCode(null)
                .posted(LocalDate.now())
                .build();
        LateBillPayment lateBillPayment = new LateBillPayment(billPayment.getDueDate(), new BigDecimal("25.00"), billPayment);

        assertThrows(InvalidLatePaymentException.class, () -> {
            processor.processSinglePayment(lateBillPayment);
        });
    }

    @Test
    @DisplayName("Test Process Single Payment when LatePayment is valid, then return processed LatePayment")


    @AfterEach
    void tearDown() {
    }
}