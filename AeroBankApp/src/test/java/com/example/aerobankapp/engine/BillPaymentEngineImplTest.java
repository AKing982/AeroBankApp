package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    private RabbitTemplate rabbitTemplate;
    private final BigDecimal PAYMENT_AMOUNT = new BigDecimal("85.00");
    private final String PAYMENT_TYPE = "ACCOUNT";
    private final LocalDate SCHEDULED_PAYMENT_DATE = LocalDate.of(2024, 5, 29);
    private final LocalDate DUE_DATE = LocalDate.of(2024, 6, 1);
    private final String PAYEE_NAME = "Payee Test";
    private final ScheduleFrequency MONTHLY = ScheduleFrequency.MONTHLY;
    private final AccountCode ACCOUNT_CODE = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
    private BillPayment TEST_PAYMENT;

    @BeforeEach
    void setUp() {

        billPaymentEngine = new BillPaymentEngineImpl(rabbitTemplate, billPaymentScheduleService, billPaymentService, billPaymentNotificationService);

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

    @AfterEach
    void tearDown() {
    }
}