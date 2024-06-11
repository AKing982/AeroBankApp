package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BillPaymentSchedulerTest {

    @InjectMocks
    private BillPaymentScheduler billPaymentScheduler;

    @BeforeEach
    void setUp() {
        billPaymentScheduler = new BillPaymentScheduler();
    }

    @Test
    @DisplayName("Test get NextPaymentDate when bill payment is null, then throw exception")
    public void testGetNextPaymentDate_whenBillPaymentIsNull_thenThrowException() {
        assertThrows(InvalidBillPaymentException.class, () -> billPaymentScheduler.getNextPaymentDate(null));
    }

    @Test
    @DisplayName("Test getNextPaymentDate when due date and payment date null, then throw exception")
    public void testGetNextPaymentDate_whenDueDateAndPaymentDateNull_thenThrowException() {
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);

        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(accountCode)
                .dueDate(null)
                .scheduledPaymentDate(null)
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .paymentID(1L)
                .build();
        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentScheduler.getNextPaymentDate(billPayment);
        });
    }

    @AfterEach
    void tearDown() {
    }
}