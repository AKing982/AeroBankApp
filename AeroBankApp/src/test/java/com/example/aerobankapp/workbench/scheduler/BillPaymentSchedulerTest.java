package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BillPaymentSchedulerTest {

    @Autowired
    private BillPaymentScheduler billPaymentScheduler;

    @Autowired
    private BillPaymentDataManager billPaymentDataManager;

    @BeforeEach
    void setUp() {

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

    @Test
    @DisplayName("Test GetNextPaymentDate_whenPaymentDateNull_useDueDate_returnNextPaymentDate")
    public void testGetNextPaymentDate_whenPaymentDateNull_useDueDate_returnNextPaymentDate() {
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);

        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(accountCode)
                .dueDate(LocalDate.of(2024, 6, 1))
                .scheduledPaymentDate(null)
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .paymentID(1L)
                .build();
        Optional<LocalDate> expected = Optional.of(LocalDate.of(2024, 7, 1));

        Optional<LocalDate> nextPaymentDate = billPaymentScheduler.getNextPaymentDate(billPayment);
        assertEquals(LocalDate.of(2024, 7, 1), nextPaymentDate.get());
        assertEquals(expected, billPaymentScheduler.getNextPaymentDate(billPayment));
    }

    @Test
    @DisplayName("Test GetNextPaymentDate when due date is null, look up last due date and return next payment date")
    public void testGetNextPaymentDate_whenDueDateIsNull_thenThrowException() {
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 1);
        BillPayment billPayment = BillPayment.builder()
                .paymentAmount(new BigDecimal("45.00"))
                .paymentType("ACCOUNT")
                .accountCode(accountCode)
                .dueDate(null)
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .isAutoPayEnabled(true)
                .payeeName("Payee Test")
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .userID(1)
                .paymentID(1L)
                .build();

        Optional<LocalDate> expected = Optional.of(LocalDate.of(2024, 7, 1));
        assertEquals(expected, billPaymentScheduler.getNextPaymentDate(billPayment));
    }

    @AfterEach
    void tearDown() {
    }
}