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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

        billPaymentEngine = new BillPaymentEngineImpl(billPaymentScheduleService, billPaymentService, billPaymentNotificationService, accountService, accountDetailsService, balanceHistoryService, accountDetailsEntityBuilder, balanceHistoryEntityBuilder);

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
        List<BillPayment> emptyBillPayments = new ArrayList<>();

        assertThrows(NonEmptyListRequiredException.class, () -> {
            billPaymentEngine.autoPayBills(emptyBillPayments);
        });
    }

    @Test
    @DisplayName("Test AutoPayBills when BillPayments objects null throw exception")
    public void testAutoPayBills_whenSingleBillPaymentNull_throwException(){
        List<BillPayment> billPaymentList = new ArrayList<>();
        billPaymentList.add(null);

        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.autoPayBills(billPaymentList);
        });
    }

    @Test
    @DisplayName("Test AutoPayBills when multiple bill payments and one null payment, skip null payment return ProcessedBillPayment list")
    public void testAutoPayBills_whenMultipleBills_skipNullPayment_returnProcessedBillPaymentList() {
        List<BillPayment> billPaymentList = new ArrayList<>();

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

        billPaymentList.add(TEST_PAYMENT);
        billPaymentList.add(null);
        billPaymentList.add(billPayment);

        List<ProcessedBillPayment> processedBillPayments = billPaymentEngine.autoPayBills(billPaymentList);

        assertNotNull(processedBillPayments);
        assertEquals(2, processedBillPayments.size());
    }


    @Test
    public void testProcessPayments_whenBillPaymentParametersInvalid_throwException() {
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

        TreeMap<LocalDate, BillPayment> billPaymentMap = new TreeMap<>();
        billPaymentMap.put(LocalDate.now(), billPaymentWithNullParameters);

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processPayments(billPaymentMap);
        });
    }

   @Test
   @DisplayName("Test Build Late Payment when bill payment is null, then throw exception")
   public void testProcessLatePayment_whenBillPaymentNull_thenThrowException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.buildLatePayment(null);
        });
   }

   @Test
   @DisplayName("Test Build Late Payment when payment date after due date, then return LateBillPayment")
   public void testProcessLatePayment_whenPaymentDateAfterDueDate_thenReturnLateBillPayment(){
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

       BigDecimal lateFee = new BigDecimal("25.00");
       LateBillPayment expected = new LateBillPayment(LocalDate.of(2024, 6, 20), lateFee, billPayment);
       LateBillPayment actual = billPaymentEngine.buildLatePayment(billPayment);

       assertEquals(expected, actual);
   }

    @Test
    public void testProcessPayments_whenLatePaymentBillParametersInvalid_throwException() {

    }

    @Test
    public void testProcessSinglePayment_whenBillPaymentIsNull_throwException() {
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
    @DisplayName("Test Process Single Payment when Payment amount is null then throw exception")
    public void testProcessSinglePayment_whenPaymentAmountNull_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(null)
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        assertThrows(NullPaymentAmountException.class, () -> {
            billPaymentEngine.processSinglePayment(billPayment);
        });
    }

    @Test
    @DisplayName("Test Process Single payment when payment date is null, then throw exception")
    public void testProcessSinglePayment_whenPaymentDateNull_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("50.00"))
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        assertThrows(IllegalDateException.class, () -> {
            billPaymentEngine.processSinglePayment(billPayment);
        });
    }

    @Test
    @DisplayName("Test Process Single payment when Payment Amount and Payment Date are valid, then return ProcessedBillPayment")
    public void testProcessSinglePayment_whenPaymentAmountAndDateValid_returnProcessedBillPayment(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("50.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 15))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        ProcessedBillPayment expected = new ProcessedBillPayment(billPayment, true, LocalDate.now());
        ProcessedBillPayment actual = billPaymentEngine.processSinglePayment(billPayment);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test processPaymentAndScheduleNextPayment when payment date after due date, then return late fee with payment amount ")
    public void testProcessPaymentAndScheduleNextPayment_whenPaymentDateIsLate_returnLateFeeWithNextPayment(){
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

        TreeMap<LocalDate, BigDecimal> expected = new TreeMap<>();
        expected.put(LocalDate.of(2024, 7, 20), BigDecimal.valueOf(75.00));

        TreeMap<LocalDate, BigDecimal> actual = billPaymentEngine.processPaymentAndScheduleNextPayment(billPayment);

        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Test processPaymentAndScheduleNextPayment when payment date before due date, then return next payment date criteria")
    public void testProcessPaymentAndScheduleNextPayment_whenPaymentDateBeforeDueDate_thenReturnNextPayment(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("50.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 6, 15))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(MONTHLY)
                .build();

        TreeMap<LocalDate, BigDecimal> expected = new TreeMap<>();
        expected.put(LocalDate.of(2024, 7, 20), BigDecimal.valueOf(50.00));
        TreeMap<LocalDate, BigDecimal> actual = billPaymentEngine.processPaymentAndScheduleNextPayment(billPayment);

        assertEquals(expected.size(), actual.size());


    }

    @Test
    public void testProcessSinglePayment_whenAutoPaymentBillParametersNull_throwException(){
        AutoPayBillPayment autoPayBillPayment = new AutoPayBillPayment(null, ACCOUNT_CODE,null, null, null, null, null, null, true, LocalDate.now());

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentEngine.processSinglePayment(autoPayBillPayment);
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
    @DisplayName("Test calculateNextPaymentDate when LocalDate is null")
    public void testCalculateNextPaymentDate_whenPaymentDateIsNull_throwException(){
        assertThrows(IllegalDateException.class, () -> {
            billPaymentEngine.calculateNextPaymentDate(null, ScheduleFrequency.MONTHLY);
        });
    }

    @Test
    @DisplayName("Test calculateNextPaymentDate when schedule frequency is null")
    public void testCalculateNextPaymentDate_whenFrequencyIsNull_throwException(){
        assertThrows(IllegalScheduleCriteriaException.class, () -> {
            billPaymentEngine.calculateNextPaymentDate(LocalDate.of(2024, 5, 19), null);
        });
    }

    @Test
    @DisplayName("Test calculateNextPaymentDate when frequency and date are null throw exception")
    public void testCalculateNextPaymentDate_whenDateAndFrequencyNull_throwException(){
        assertThrows(IllegalArgumentException.class, () -> {
            billPaymentEngine.calculateNextPaymentDate(null, null);
        });
    }

    @Test
    @DisplayName("Test calculateNextPaymentDate when frequency is monthly")
    public void testCalculateNextPaymentDate_whenMonthly_returnNextPaymentDate(){

        LocalDate paymentDate = LocalDate.of(2024, 5, 29);
        ScheduleFrequency monthly = ScheduleFrequency.MONTHLY;

        LocalDate expectedNextPaymentDate = LocalDate.of(2024, 6, 29);
        LocalDate actual = billPaymentEngine.calculateNextPaymentDate(paymentDate, monthly);

        assertNotNull(actual);
        assertEquals(expectedNextPaymentDate, actual);
    }

    @Test
    @DisplayName("Test calculateNextPaymentDate when frequency is weekly")
    public void testCalculateNextPaymentDate_whenWeekly_returnNextPaymentDate(){
        LocalDate paymentDate = LocalDate.of(2024, 5, 29);
        ScheduleFrequency weekly = ScheduleFrequency.WEEKLY;

        LocalDate expectedNextPaymentDate = LocalDate.of(2024, 6, 5);
        LocalDate actual = billPaymentEngine.calculateNextPaymentDate(paymentDate, weekly);

        assertNotNull(actual);
        assertEquals(expectedNextPaymentDate, actual);
    }

    @Test
    @DisplayName("Test calculateNextPaymentDate when frequency is bi weekly")
    public void testCalculateNextPaymentDate_whenBiWeekly_returnNextPaymentDate(){
        LocalDate paymentDate = LocalDate.of(2024, 5, 29);
        ScheduleFrequency biweekly = ScheduleFrequency.BI_WEEKLY;

        LocalDate expectedNextPaymentDate = LocalDate.of(2024, 6, 12);
        LocalDate actual = billPaymentEngine.calculateNextPaymentDate(paymentDate, biweekly);

        assertNotNull(actual);
        assertEquals(expectedNextPaymentDate, actual);
    }

    @Test
    @DisplayName("Test GetNextPaymentDateFromPayment when payment is null")
    public void testGetNextPaymentDateFromPayment_whenPaymentNull_throwException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentEngine.getNextPaymentDateFromPayment(null);
        });
    }

    @Test
    @DisplayName("Test GetNextPaymentDateFromPayment when payment date is null use the due date use monthly")
    public void testGetNextPaymentDateFromPayment_whenPaymentDateNull_returnNextPayment(){

        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
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

       LocalDate expectedDate = LocalDate.of(2024, 7, 20);
       LocalDate actual = billPaymentEngine.getNextPaymentDateFromPayment(billPayment);

       assertNotNull(actual);
       assertEquals(expectedDate, actual);
    }

    @Test
    @DisplayName("Test GetNextPaymentDateFromPayment when frequency is null and payment date throw exception")
    public void testGetNextPaymentDateFromPayment_whenFrequencyNull_throwException(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(null)
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .isProcessed(true)
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .schedulePaymentID(1L)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(null)
                .paymentID(1L)
                .build();

        assertThrows(IllegalScheduleCriteriaException.class, () -> {
            billPaymentEngine.getNextPaymentDateFromPayment(billPayment);
        });
    }

    @Test
    @DisplayName("Test GetNextPaymentDateFromPayment when payment and frequency as weekly return next payment date")
    public void testGetNextPaymentDateFromPayment_whenPaymentAndFrequencyAsWeekly_returnNextPaymentDate(){
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .scheduledPaymentDate(LocalDate.of(2024, 5, 19))
                .dueDate(LocalDate.of(2024, 6, 20))
                .paymentType("ACCOUNT")
                .isProcessed(true)
                .scheduleStatus(ScheduleStatus.PENDING)
                .isAutoPayEnabled(true)
                .accountCode(ACCOUNT_CODE)
                .schedulePaymentID(1L)
                .payeeName(PAYEE_NAME)
                .scheduleFrequency(ScheduleFrequency.WEEKLY)
                .paymentID(1L)
                .build();

        LocalDate expectedNextPaymentDate = LocalDate.of(2024, 5, 26);
        LocalDate actual = billPaymentEngine.getNextPaymentDateFromPayment(billPayment);

        assertNotNull(actual);
        assertEquals(expectedNextPaymentDate, actual);
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