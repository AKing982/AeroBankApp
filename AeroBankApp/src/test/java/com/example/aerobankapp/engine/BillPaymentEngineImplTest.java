package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountDetailsEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentEngineImplTest {

    @InjectMocks
    private BillPaymentEngineImpl billPaymentEngine;

    @Mock
    private BillPaymentScheduleService billPaymentScheduleService;

    @Mock
    private BillPaymentService billPaymentService;

    @Mock
    private BillPaymentNotificationService billPaymentNotificationService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountDetailsService accountDetailsService;

    @Mock
    private BalanceHistoryService balanceHistoryService;

    @Mock
    private EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder;

    @Mock
    private EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder;

    @Mock
    private RabbitTemplate rabbitTemplate;
    private final BigDecimal PAYMENT_AMOUNT = new BigDecimal("85.00");
    private final String PAYMENT_TYPE = "ACCOUNT";
    private final LocalDate SCHEDULED_PAYMENT_DATE = LocalDate.of(2024, 5, 29);
    private final LocalDate DUE_DATE = LocalDate.of(2024, 6, 1);
    private final String PAYEE_NAME = "Payee Test";
    private final ScheduleFrequency MONTHLY = ScheduleFrequency.MONTHLY;
    private final AccountCode ACCOUNT_CODE = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
    private BillPayment TEST_PAYMENT;

    @Mock
    private EntityBuilder<BillPaymentEntity, BillPayment> billPaymentEntityBillPaymentEntityBuilder;

    @BeforeEach
    void setUp() {

        billPaymentEngine = new BillPaymentEngineImpl(rabbitTemplate, billPaymentScheduleService, billPaymentService, billPaymentNotificationService, accountService, accountDetailsService, balanceHistoryService, accountDetailsEntityBuilder, balanceHistoryEntityBuilder);

        TEST_PAYMENT = BillPayment.builder()
                .paymentAmount(PAYMENT_AMOUNT)
                .paymentType(PAYMENT_TYPE)
                .scheduledPaymentDate(SCHEDULED_PAYMENT_DATE)
                .isAutoPayEnabled(true)
                .dueDate(DUE_DATE)
                .payeeName(PAYEE_NAME)
                .posted(LocalDate.now())
                .scheduleFrequency(MONTHLY)
                .accountCode(ACCOUNT_CODE)
                .build();
    }

    @Test
    public void testAutoPayBills_whenEmptyBillPayments_throwNonEmptyListException() {

        // Arrange
        List<AutoPayBillPayment> emptyBillPayments = Collections.emptyList();

        assertThrows(NonEmptyListRequiredException.class, () -> {
            billPaymentEngine.autoPayBills(emptyBillPayments);
        });
    }

    @Test
    public void testAutoPayBills_whenBillPaymentsHaveAutoPayEnabled_return_ProcessedBillPayment(){

        // Arrange
        AutoPayBillPayment autoPayBillPayment = new AutoPayBillPayment(PAYEE_NAME, ACCOUNT_CODE, PAYMENT_AMOUNT, PAYMENT_TYPE, DUE_DATE, SCHEDULED_PAYMENT_DATE, ScheduleStatus.PENDING, MONTHLY, true, LocalDate.now());
        List<AutoPayBillPayment> billPayments = Collections.singletonList(autoPayBillPayment);
        ProcessedBillPayment processedBillPayment = new ProcessedBillPayment(TEST_PAYMENT, true);

        // Act
        List<ProcessedBillPayment> actual = billPaymentEngine.autoPayBills(billPayments);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void testProcessPayments_whenBillPaymentsListIsEmpty_throwException(){
        List<BillPayment> emptyList = Collections.emptyList();

        assertThrows(NonEmptyListRequiredException.class, () -> {
            billPaymentEngine.processPayments(emptyList);
        });
    }

    @Test
    public void testProcessPayments_whenBillPaymentParametersInvalid_throwException(){
        BillPayment billPaymentWithNullParameters = BillPayment.builder()
                .paymentAmount(null)
                .paymentType("ACCOUNT")
                .dueDate(null)
                .scheduledPaymentDate(null)
                .isAutoPayEnabled(false)
                .scheduleFrequency(null)
                .payeeName(null)
                .accountCode(null)
                .scheduleStatus(null)
                .build();

        List<BillPayment> billPaymentList = Collections.singletonList(billPaymentWithNullParameters);


        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processPayments(billPaymentList);
        });
    }

    @Test
    public void testProcessPayments_whenAutoPayBillParametersInvalid_throwException(){
        AutoPayBillPayment autoPayBillPayment = new AutoPayBillPayment(null, ACCOUNT_CODE,null, null, null, null, null, null, true, LocalDate.now());
        List<AutoPayBillPayment> autoPayBillPaymentList = Collections.singletonList(autoPayBillPayment);

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processPayments(autoPayBillPaymentList);
        });
    }

    @Test
    public void testProcessPayments_whenLatePaymentBillParametersInvalid_throwException(){
        LateBillPayment lateBillPayment = new LateBillPayment(null, ACCOUNT_CODE,null, null, null, null, null, null, true, LocalDate.now(),
                LocalDate.of(2024, 5, 19), LocalDate.of(2024, 5, 19), new BigDecimal("12.00"));

        List<LateBillPayment> lateBillPaymentList = Collections.singletonList(lateBillPayment);

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processPayments(lateBillPaymentList);
        });
    }

    @Test
    public void testProcessSinglePayment_whenBillPaymentIsNull_throwException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.processSinglePayment(null);
        });
    }

    @Test
    public void testProcessSinglePayment_whenBillPaymentParametersNull_throwException(){
        BillPayment billPaymentWithNullParameters = BillPayment.builder()
                .paymentAmount(null)
                .paymentType("ACCOUNT")
                .dueDate(null)
                .scheduledPaymentDate(null)
                .isAutoPayEnabled(false)
                .scheduleFrequency(null)
                .payeeName(null)
                .accountCode(null)
                .scheduleStatus(null)
                .build();

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
               billPaymentEngine.processSinglePayment(billPaymentWithNullParameters);
           });
    }

    @Test
    public void testProcessSinglePayment_whenAutoPaymentBillParametersNull_throwException(){
        AutoPayBillPayment autoPayBillPayment = new AutoPayBillPayment(null, ACCOUNT_CODE,null, null, null, null, null, null, true, LocalDate.now());

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processSinglePayment(autoPayBillPayment);
        });
    }

    @Test
    public void testProcessSinglePayment_whenLatePaymentBillParametersNull_throwException(){
        LateBillPayment lateBillPayment = new LateBillPayment(null, ACCOUNT_CODE,null, null, null, null, null, null, true, LocalDate.now(),
                LocalDate.of(2024, 5, 19), LocalDate.of(2024, 5, 19), new BigDecimal("12.00"));

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processSinglePayment(lateBillPayment);
        });
    }

    @Test
    public void testProcessSinglePayment_whenAutoPayPaymentValid_returnProcessedBillPayment(){
        AutoPayBillPayment autoPayBillPayment = new AutoPayBillPayment(PAYEE_NAME, ACCOUNT_CODE, PAYMENT_AMOUNT, PAYMENT_TYPE, DUE_DATE, SCHEDULED_PAYMENT_DATE, ScheduleStatus.PENDING, MONTHLY, true, LocalDate.now());
        ProcessedBillPayment expectedProcessedBillPayment = new ProcessedBillPayment(autoPayBillPayment, true);

        ProcessedBillPayment processedBillPayment = billPaymentEngine.processSinglePayment(autoPayBillPayment);

        assertEquals(expectedProcessedBillPayment.getBillPayment(), processedBillPayment.getBillPayment());
        assertEquals(expectedProcessedBillPayment.isComplete(), processedBillPayment.isComplete());
    }


    @Test
    public void testValidatePaymentDatePriorToDueDate_nullPaymentDate_validPaymentDueDate_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        assertThrows(InvalidDateException.class, () -> {
            billPaymentEngine.validatePaymentDatePriorDueDate(billPayment);
        });
    }

    @Test
    public void testValidatePaymentDatePriorDueDate_nullDueDate_validPaymentDate_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(SCHEDULED_PAYMENT_DATE)
                .dueDate(null)
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        assertThrows(InvalidDateException.class, () -> {
            billPaymentEngine.validatePaymentDatePriorDueDate(billPayment);
        });
    }

    @Test
    public void testValidatePaymentDatePriorDueDate_whenPaymentDatePriorDueDate_returnTrue(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 15))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        boolean isValidPaymentDate = billPaymentEngine.validatePaymentDatePriorDueDate(billPayment);

        assertTrue(isValidPaymentDate);
    }

    @Test
    public void testValidatePaymentDatePriorDueDate_whenPaymentDatePastDueDate_returnFalse(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        boolean isInvalidPayment = billPaymentEngine.validatePaymentDatePriorDueDate(billPayment);

        assertFalse(isInvalidPayment);
    }

    @Test
    public void testGetProcessedPayment_nullBillPayment_throwException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.getProcessedPayment(null);
        });
    }


    @Test
    public void testGetProcessedPayment_whenPaymentDateAfterDueDate_returnProcessedBillWithFees(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

       // ProcessedBillPayment processedBillPayment = new ProcessedBillPayment()
    }

    @Test
    public void testPaymentVerification_whenProcessedBillPaymentIsNull_throwException(){
        assertThrows(InvalidProcessedBillPaymentException.class, () -> {
            billPaymentEngine.paymentVerification(null);
        });
    }

    @Test
    public void testPaymentVerification_whenIsCompleteTrue_returnTrue(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        ProcessedBillPayment processedBillPayment = new ProcessedBillPayment(billPayment, true, LocalDate.now());

        boolean isVerified = billPaymentEngine.paymentVerification(processedBillPayment);

        assertTrue(isVerified);
    }

    @Test
    public void testPaymentVerification_whenLastProcessedDateIsNull_returnFalse(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        ProcessedBillPayment processedBillPayment = new ProcessedBillPayment(billPayment, true, null);

        boolean paymentIsVerified = billPaymentEngine.paymentVerification(processedBillPayment);

        assertFalse(paymentIsVerified);
    }

    @Test
    public void testPaymentVerification_whenPaymentIDNotFound_returnFalse(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        ProcessedBillPayment payment = new ProcessedBillPayment(billPayment, true, LocalDate.now());

        boolean isVerified = billPaymentEngine.paymentVerification(payment);

        assertFalse(isVerified);
    }

    @Test
    public void testPaymentVerification_whenPaymentID_returnTrue(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 25))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .isProcessed(true)
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .schedulePaymentID(1L)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .paymentID(1L)
                .build();

        BillPaymentEntity billPaymentEntity = billPaymentEntityBillPaymentEntityBuilder.createEntity(billPayment);

        billPaymentService.save(billPaymentEntity);

        ProcessedBillPayment payment = new ProcessedBillPayment(billPayment, true, LocalDate.now());

        // Mock the behavior of billPaymentService

        boolean isVerified = billPaymentEngine.paymentVerification(payment);

        assertTrue(isVerified);
    }

    @Test
    public void testGetNextPaymentDate_whenBillPaymentNull_throwException(){

        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.getNextPaymentDate(null);
        });
    }

    @Test
    public void testGetNextPaymentDate_whenScheduledPaymentDateNull_returnNextDueDate(){

        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        LocalDate nextPaymentDate = billPaymentEngine.getNextPaymentDate(billPayment);

        assertEquals(LocalDate.of(2024, 7, 20), nextPaymentDate);
    }

    @Test
    public void testGetNextPaymentDate_whenScheduledPaymentDateValid_returnNextPaymentDate(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 15))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        LocalDate nextPaymentDate = billPaymentEngine.getNextPaymentDate(billPayment);

        assertNotNull(nextPaymentDate);
        assertEquals(LocalDate.of(2024, 7, 15), nextPaymentDate);
    }

    @Test
    public void testGetLastPaymentDate_whenBillPaymentIsNull_throwException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.getLastPaymentDate(null);
        });
    }

    @Test
    public void testGetLastPaymentDate_whenScheduledPaymentDateNull_returnDueDate(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        LocalDate lastPaymentDate = billPaymentEngine.getLastPaymentDate(billPayment);

        assertNotNull(lastPaymentDate);
        assertEquals(LocalDate.of(2024, 6, 20), lastPaymentDate);
    }

    @Test
    public void testGetLastPaymentDate_whenDueDateAndScheduledPaymentDateNull_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
                .dueDate(null)
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        assertThrows(InvalidDateException.class, () -> {
            billPaymentEngine.getLastPaymentDate(billPayment);
        });
    }

    @Test
    public void testGetLastPaymentDate_whenDueDateIsNull_returnScheduledPaymentAsLastDate(){
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

        LocalDate lastPaymentDate = billPaymentEngine.getLastPaymentDate(billPayment);

        assertNotNull(lastPaymentDate);
        assertEquals(LocalDate.of(2024, 5, 19), lastPaymentDate);
    }





    @AfterEach
    void tearDown() {
    }
}