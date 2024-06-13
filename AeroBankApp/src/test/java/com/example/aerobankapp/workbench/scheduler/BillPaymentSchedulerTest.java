package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.exceptions.IllegalDateException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentIDException;
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
import java.util.TreeMap;

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
    @DisplayName("Test getNextPaymentDate when payment date is null, use last due date, return next payment")
    public void testGetNextPaymentDate_whenPaymentDateIsNull_returnNextPayment() {
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

        Optional<LocalDate> expectedPaymentDate = Optional.of(LocalDate.of(2024, 7, 1));
        Optional<LocalDate> nextPaymentDate = billPaymentScheduler.getNextPaymentDate(billPayment);

        assertEquals(expectedPaymentDate, nextPaymentDate);
        assertNotNull(nextPaymentDate);
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
    @DisplayName("Test calculateNextDueDate when previous date is null, throw exception")
    public void testCalculateNextDueDate_whenPreviousDateIsNull_thenThrowException() {
        assertThrows(IllegalDateException.class, () -> {
            billPaymentScheduler.calculateNextDueDate(null, null);
        });
    }

    @Test
    @DisplayName("Test calculateNextDueDate when previous date is valid, return next due date")
    public void testCalculateNextDueDate_whenPreviousDateIsValid_returnNextDueDate() {
        LocalDate previousDueDate = LocalDate.of(2024, 6, 1);
        Optional<LocalDate> nextDueDate = billPaymentScheduler.calculateNextDueDate(previousDueDate, ScheduleFrequency.MONTHLY);
        assertEquals(LocalDate.of(2024, 7, 1), nextDueDate.get());
    }

    @Test
    @DisplayName("Test getNextPaymentDetails when payment is null, throw exception")
    public void testGetNextPaymentDetails_whenPaymentIsNull_thenThrowException() {
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentScheduler.getNextPaymentDetails(null);
        });
    }

    @Test
    @DisplayName("Test getNextPaymentDetails when payment date and due date is null, then throw exception")
    public void testGetNextPaymentDetails_whenDueDateAndPaymentDateIsNull_thenThrowException() {
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

        assertThrows(IllegalDateException.class, () -> {
            billPaymentScheduler.getNextPaymentDetails(billPayment);
        });
    }

    @Test
    @DisplayName("Test getNextPaymentDetails when due date is null, fetch latest due date, return paymentDetails")
    public void testGetNextPaymentDetails_whenDueDateIsNull_fetchLatestDueDate_thenReturnPaymentDetails(){
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

        TreeMap<String, LocalDate> expectedPaymentDetails = new TreeMap<>();
        expectedPaymentDetails.put("nextDueDate", LocalDate.of(2024, 7, 1));
        expectedPaymentDetails.put("nextPaymentDate", LocalDate.of(2024, 6, 28));

        TreeMap<String, LocalDate> actualPaymentDetails = billPaymentScheduler.getNextPaymentDetails(billPayment);
        assertEquals(expectedPaymentDetails, actualPaymentDetails);
        assertEquals(expectedPaymentDetails.get("nextDueDate"), actualPaymentDetails.get("nextDueDate"));
        assertEquals(expectedPaymentDetails.get("nextPaymentDate"), actualPaymentDetails.get("nextPaymentDate"));
    }

    @Test
    @DisplayName("Test GetNextPaymentDetails when payment date is null, then fetch the last payment date, and return next payment")
    public void testGetNextPaymentDetails_when_paymentDateIsNull_fetchLastPaymentDate_thenReturnNextPayment(){
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

        TreeMap<String, LocalDate> expectedPaymentDetails = new TreeMap<>();
        expectedPaymentDetails.put("nextDueDate", LocalDate.of(2024, 7, 1));
        expectedPaymentDetails.put("nextPaymentDate", LocalDate.of(2024, 6, 30));

        TreeMap<String, LocalDate> actualPaymentDetails = billPaymentScheduler.getNextPaymentDetails(billPayment);
        assertEquals(expectedPaymentDetails, actualPaymentDetails);
        assertEquals(expectedPaymentDetails.get("nextDueDate"), actualPaymentDetails.get("nextDueDate"));
        assertEquals(expectedPaymentDetails.get("nextPaymentDate"), actualPaymentDetails.get("nextPaymentDate"));
    }

    @Test
    @DisplayName("Test getPreviousPaymentDate when bill payment is null, then throw exception")
    public void testGetPreviousPaymentDate_whenBillPaymentIsNull_fetchPreviousPaymentDate_thenReturnPreviousPayment(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentScheduler.getPreviousPaymentDate(null);
        });
    }

    @Test
    @DisplayName("Test getPreviousPaymentDate when paymentID is invalid, then throw exception")
    public void testGetPreviousPaymentDate_whenPaymentIDIsInvalid_fetchPreviousPaymentId_thenReturnPreviousPayment(){
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
                .paymentID(-1L)
                .build();
        assertThrows(InvalidBillPaymentIDException.class, () -> {
            billPaymentScheduler.getPreviousPaymentDate(billPayment);
        });
    }

    @Test
    @DisplayName("Test GetPreviousPaymentDate when payment id is valid, return Previous PaymentDate")
    public void testGetPreviousPaymentDate_whenPaymentIdIsValid_fetchPreviousPaymentId_thenReturnPreviousPayment(){
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

        Optional<LocalDate> expectedPreviousPaymentDate = Optional.of(LocalDate.of(2024, 4, 28));
        Optional<LocalDate> actualPreviousPaymentDate = billPaymentScheduler.getPreviousPaymentDate(billPayment);

        assertEquals(expectedPreviousPaymentDate, actualPreviousPaymentDate);
    }

    @Test
    @DisplayName("Test SchedulePayment when bill payment is null, then throw exception")
    public void testSchedulePayment_whenBillPaymentIsNull_fetchSchedulePayment_thenThrowException(){
        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentScheduler.schedulePayment(null);
        });
    }

    @Test
    @DisplayName("Test SchedulePayment when payment date is null, fetch payment date then return true")
    public void testSchedulePayment_whenPaymentDateIsNull_fetchSchedulePayment_thenReturnTrue(){
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
                .paymentID(-1L)
                .build();

        boolean result = billPaymentScheduler.schedulePayment(billPayment);
        assertTrue(result);
    }

    @AfterEach
    void tearDown() {
    }
}