package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidBalanceException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.BalanceHistoryService;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.data.BalanceHistoryDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentHistoryDataManager;
import com.example.aerobankapp.workbench.data.LatePaymentDataManager;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import com.example.aerobankapp.workbench.utilities.notifications.ProcessedBillPaymentNotificationSender;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import com.example.aerobankapp.workbench.verification.ProcessedBillPaymentVerification;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BillPaymentProcessorTest {

    @InjectMocks
    private BillPaymentProcessor billPaymentProcessor;

    @Mock
    private BillPaymentScheduler billPaymentScheduler;

    @Mock
    private BillPaymentHistoryDataManager billPaymentHistoryDataManager;

    @Mock
    private BalanceHistoryDataManager balanceHistoryDataManager;

    @Mock
    private ProcessedBillPaymentVerification processedBillPaymentVerification;

    @Mock
    private ProcessedBillPaymentNotificationSender processedBillPaymentNotificationSender;

    @Spy
    private AccountNotificationUtil accountNotificationUtil;

    @Mock
    private ConfirmationNumberGenerator confirmationNumberGenerator;

    @Mock
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Autowired
    private AccountDataManager accountDataManager;

    @Autowired
    private LatePaymentDataManager latePaymentDataManager;

    @Mock
    private PaymentVerifier<BillPayment> paymentVerifier;


    private AccountCode mockAccountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);



    @BeforeEach
    void setUp() {
        billPaymentProcessor = new BillPaymentProcessor(confirmationNumberGenerator, referenceNumberGenerator, accountDataManager, balanceHistoryDataManager, billPaymentHistoryDataManager, processedBillPaymentVerification, billPaymentScheduler, processedBillPaymentNotificationSender, latePaymentDataManager);
        mockAccountCode = mock(AccountCode.class);
        mockStatic(AccountNotificationUtil.class);
    }

    @Test
    @DisplayName("Test ProcessSinglePayment when Bill Payment is null, then throw exception")
    public void testProcessSinglePayment_whenBillPaymentIsNull_thenThrowException() {
        assertThrows(InvalidBillPaymentException.class, () -> billPaymentProcessor.processSinglePayment(null));
    }

    @Test
    @DisplayName("Test ProcessSinglePayment when payment date is null, then throw exception")
    public void testProcessSinglePayment_whenPaymentDateIsNull_thenThrowException() {
        BillPayment billPayment = BillPayment.builder()
                        .paymentAmount(new BigDecimal("45.00"))
                        .paymentType("ACCOUNT")
                        .accountCode(mockAccountCode)
                        .dueDate(LocalDate.of(2024, 6, 1))
                        .scheduledPaymentDate(null)
                        .isAutoPayEnabled(true)
                        .payeeName("Payee Test")
                        .scheduleFrequency(ScheduleFrequency.MONTHLY)
                        .scheduleStatus(ScheduleStatus.PENDING)
                        .isProcessed(false)
                        .userID(1)
                        .build();


        assertThrows(InvalidBillPaymentException.class, () -> billPaymentProcessor.processSinglePayment(billPayment));
    }

    @Test
    @DisplayName("Test ProcessSinglePayment when payment is valid and No Account Balance Found, then throw Exception")
    public void testProcessSinglePayment_whenPaymentIsValid_NoAccountBalance_thenThrowException() {
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(mockAccountCode)
                .dueDate(LocalDate.of(2024, 6, 1))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .build();

        when(accountDataManager.getCurrentAccountBalance(1)).thenReturn(null);

        assertThrows(InvalidBalanceException.class, () -> billPaymentProcessor.processSinglePayment(billPayment));
    }

    @Test
    @DisplayName("Test ProcessSinglePayment when payment is valid, valid account balance, then return ProcessedPayment")
    public void testProcessSinglePayment_whenPaymentIsValid_ValidAccountBalance_thenReturnProcessedPayment() {
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(accountCode)
                .dueDate(LocalDate.of(2024, 6, 1))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .build();

        ProcessedBillPayment expected = new ProcessedBillPayment();
        //0  doReturn(expected).when(billPaymentProcessor).buildProcessedBillPayment(any(BillPayment.class), any(LocalDate.class));

        LocalDate nextPaymentDate = LocalDate.of(2024, 6, 29);
        doReturn(Optional.of(nextPaymentDate)).when(billPaymentScheduler).getNextPaymentDate(any(BillPayment.class));

        ProcessedBillPayment result = billPaymentProcessor.processSinglePayment(billPayment);
        assertNotNull(result);
        assertEquals(nextPaymentDate, result.getNextPaymentDate());
    }

    @Test
    @DisplayName("Test processPaymentAndScheduleNextPayment when bill payment is null, then throw exception")
    public void testProcessPaymentAndScheduleNextPayment_whenBillPaymentNull_thenThrowException() {
        assertThrows(InvalidBillPaymentException.class, () -> billPaymentProcessor.processPaymentAndScheduleNextPayment(null));
    }

    @Test
    @DisplayName("Test processPaymentAndScheduleNextPayment when payment date after due date, then return next schedule date with payment amount with fee")
    public void testProcessPaymentAndScheduleNextPayment_whenPaymentDateAfterDueDate_thenReturnNextScheduleDateWithPaymentAmountWithFee(){
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(accountCode)
                .dueDate(LocalDate.of(2024, 6, 1))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .build();

        TreeMap<LocalDate, BigDecimal> expected = new TreeMap<>();
        expected.put(LocalDate.of(2024, 6, 1), BigDecimal.valueOf(70.00));

        when(billPaymentScheduler.validatePaymentDatePriorDueDate(billPayment)).thenReturn(false);
        TreeMap<LocalDate, BigDecimal> result = billPaymentProcessor.processPaymentAndScheduleNextPayment(billPayment);

        assertNotNull(result);
        assertEquals(expected, result);
        assertEquals(expected.size(), result.size());
    }

    @AfterEach
    void tearDown() {
    }
}
