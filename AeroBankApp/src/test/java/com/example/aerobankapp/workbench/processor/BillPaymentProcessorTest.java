package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidBalanceException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class BillPaymentProcessorTest {

    @InjectMocks
    private BillPaymentProcessor billPaymentProcessor;

    @Mock
    private AccountDataManager accountDataManager;

    @Mock
    private PaymentVerifier<BillPayment> paymentVerifier;


    private AccountCode mockAccountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);



    @BeforeEach
    void setUp() {
        billPaymentProcessor = new BillPaymentProcessor(accountDataManager, paymentVerifier);
        mockAccountCode = mock(AccountCode.class);
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

        when(accountDataManager.getCurrentAccountBalance(1)).thenReturn(BigDecimal.valueOf(120));

        ProcessedBillPayment processedBillPayment = billPaymentProcessor.processSinglePayment(billPayment);
        assertNotNull(processedBillPayment);
        assertEquals(billPayment, processedBillPayment);
        assertEquals(billPayment.getScheduledPaymentDate(), processedBillPayment.getBillPayment().getScheduledPaymentDate());
        assertEquals(billPayment.getPaymentAmount(), processedBillPayment.getBillPayment().getPaymentAmount());
        assertEquals(billPayment.getPaymentType(), processedBillPayment.getBillPayment().getPaymentType());
        assertEquals(billPayment.getAccountCode(), processedBillPayment.getBillPayment().getAccountCode());
        assertEquals(billPayment.getDueDate(), processedBillPayment.getBillPayment().getDueDate());
        assertEquals(billPayment.getScheduleFrequency(), processedBillPayment.getBillPayment().getScheduleFrequency());
        assertEquals(billPayment.getScheduleStatus(), processedBillPayment.getBillPayment().getScheduleStatus());
        assertEquals(billPayment.getUserID(), processedBillPayment.getBillPayment().getUserID());
    }

    @AfterEach
    void tearDown() {
    }
}
