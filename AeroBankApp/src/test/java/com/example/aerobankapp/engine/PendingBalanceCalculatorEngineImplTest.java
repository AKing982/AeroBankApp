package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.InvalidDateException;
import com.example.aerobankapp.model.AccountDetails;

import com.example.aerobankapp.model.TransactionCriteria;
import com.example.aerobankapp.scheduler.ScheduleType;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class PendingBalanceCalculatorEngineImplTest {

    @Autowired
    private PendingBalanceCalculatorEngineImpl pendingBalanceCalculatorEngine;

    private final BigDecimal AMOUNT = new BigDecimal("120.00");
    private final String DESCRIPTION = "Test";
    private final LocalTime SCHEDULED_TIME = LocalTime.now();
    private final LocalDate SCHEDULED_DATE = LocalDate.of(2024, 5, 29);
    private final LocalDate POSTED = LocalDate.now();
    private final ScheduleType MONTHLY = ScheduleType.MONTHLY;


    @BeforeEach
    void setUp() {

    }

    @Test
    public void testCalculatePendingAmount(){
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);
        List<TransactionCriteria> criteriaList = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        BigDecimal result = pendingBalanceCalculatorEngine.calculatePendingAmount(accountDetails, criteriaList);

        assertEquals(new BigDecimal("120.00"), result);
        assertEquals(new BigDecimal("50.00"), accountDetails.getPending());
    }

    @Test
    public void testCalculatePendingAmount_EmptyTransactions_ShouldThrowException(){
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);
        List<TransactionCriteria> transactionCriteria = Arrays.asList();

        assertThrows(IllegalArgumentException.class, () -> {
            pendingBalanceCalculatorEngine.calculatePendingAmount(accountDetails, transactionCriteria);
        });
    }

    @Test
    public void testCalculatePendingAmount_NullAccountDetails_ShouldThrowException(){
        List<TransactionCriteria> criteriaList = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pendingBalanceCalculatorEngine.calculatePendingAmount(null, criteriaList);
        });
    }

    @Test
    public void testGetPendingAmountSum_whenTransactionCriteriaListEmpty_throwException(){
        List<TransactionCriteria> emptyTransactions = Collections.emptyList();

        assertThrows(IllegalArgumentException.class, () -> {
            pendingBalanceCalculatorEngine.getPendingAmountSum(emptyTransactions);
        });
    }

    @Test
    public void testGetPendingAmountSum_whenAmountCriteriaIsNull_returnZero(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(null, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        BigDecimal pending = pendingBalanceCalculatorEngine.getPendingAmountSum(transactionCriteria);

        assertEquals(BigDecimal.ZERO, pending);
    }

    @Test
    public void testGetPendingAmountSum_whenCriteriaIsValid_returnPendingAmount(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        BigDecimal pending = pendingBalanceCalculatorEngine.getPendingAmountSum(transactionCriteria);

        assertEquals(new BigDecimal("120.00"), pending);
    }

    @Test
    public void testGetPendingAmountByDateRange_whenStartDateIsNull_throwException(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        LocalDate endDate = LocalDate.of(2024, 6, 5);

       assertThrows(InvalidDateException.class, () -> {
           pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, null, endDate);
       });
    }

    @Test
    public void testGetPendingAmountByDateRange_whenEndDateIsNull_throwException(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        LocalDate startDate = LocalDate.now();

        assertThrows(InvalidDateException.class, () -> {
            pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, startDate, null);
        });
    }

    @Test
    public void testGetPendingAmountByDateRange_whenTransactionCriteriaListEmpty_returnZero(){
        List<TransactionCriteria> emptyList = Collections.emptyList();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        BigDecimal pending = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(emptyList, startDate, endDate);

        assertEquals(BigDecimal.ZERO, pending);
    }

    @Test
    public void testGetPendingAmountByDateRange_whenAmountCriteriaIsNull_throwException(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(null, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2024, 6, 15);

        assertThrows(IllegalArgumentException.class, () -> {
            pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, startDate, endDate);
        });
    }

    @Test
    public void testGetPendingAmountByDateRange_whenScheduledDateNotBetweenStartDateAndEndDate_returnZero(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate endDate = LocalDate.of(2024, 5, 10);

        BigDecimal pending = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, startDate, endDate);

        assertNotNull(pending);
        assertEquals(BigDecimal.ZERO, pending);
    }

    @Test
    public void testGetPendingAmountByDateRange_whenScheduledDateBetweenStartAndEndDate_returnPending(){
        LocalDate scheduleDate = LocalDate.of(2024, 5, 29);
        LocalDate startDate = LocalDate.of(2024, 5, 25);
        LocalDate endDate = LocalDate.of(2024, 6, 5);
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, scheduleDate, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        BigDecimal pending = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, startDate, endDate);

        assertNotNull(pending);
        assertEquals(new BigDecimal("120.00"), pending);
    }

    @Test
    public void testGetPendingAmountByDateRange_whenScheduledDateIsNull_throwException(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, null, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2024, 6, 1);

        assertThrows(InvalidDateException.class, () -> {
            pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteria, startDate, endDate);
        });
    }

    @ParameterizedTest
    @MethodSource("provideTransactionCriteriaScheduleLists")
    @DisplayName("Test getPendingAmountByDateRange with various transaction criteria lists")
    void testGetPendingAmountByDateRange(List<TransactionCriteria> transactionCriteriaList, LocalDate startDate, LocalDate endDate, BigDecimal expectedSum) {
        BigDecimal result = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteriaList, startDate, endDate);

        assertEquals(expectedSum, result);
    }

    static Stream<Arguments> provideTransactionCriteriaScheduleLists() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 1", null, LocalDate.of(2023, 5, 1), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("200.00"), "Description 2", null, LocalDate.of(2023, 5, 10), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("300.00"), "Description 3", null, LocalDate.of(2023, 6, 1), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), new BigDecimal("300.00")),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("50.00"), "Description 1", null, LocalDate.of(2023, 5, 5), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("150.00"), "Description 2", null, LocalDate.of(2023, 5, 15), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), new BigDecimal("200.00")),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 1", null, LocalDate.of(2023, 4, 1), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("200.00"), "Description 2", null, LocalDate.of(2023, 4, 10), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), BigDecimal.ZERO),
                Arguments.of(Collections.emptyList(), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), BigDecimal.ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionCriteriaListsStartDate")
    @DisplayName("Test getPendingAmountByDateRange when scheduled date equals start date and before end end date")
    void testGetPendingAmountByDateRange_whenScheduleDateEqualToStartDateAndBeforeEndDate(List<TransactionCriteria> transactionCriteriaList, LocalDate startDate, LocalDate endDate, BigDecimal expectedSum) {
        BigDecimal result = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteriaList, startDate, endDate);
        assertEquals(expectedSum, result);
    }

    static Stream<Arguments> provideTransactionCriteriaListsStartDate() {
        return Stream.of(
                // Case where scheduledDate is equal to startDate and before endDate
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 1", null, LocalDate.of(2023, 5, 1), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("200.00"), "Description 2", null, LocalDate.of(2023, 5, 1), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), new BigDecimal("300.00")),
                // Other test cases
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("50.00"), "Description 1", null, LocalDate.of(2023, 5, 5), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("150.00"), "Description 2", null, LocalDate.of(2023, 5, 15), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), new BigDecimal("200.00")),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 1", null, LocalDate.of(2023, 4, 1), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("200.00"), "Description 2", null, LocalDate.of(2023, 4, 10), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), BigDecimal.ZERO),
                Arguments.of(Collections.emptyList(), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), BigDecimal.ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionCriteriaWithEndDate")
    @DisplayName("Test getPendingAmountByDateRange when scheduledDate equals endDate and is after startDate")
    void testGetPendingAmountByDateRange_WithEndDate(List<TransactionCriteria> transactionCriteriaList, LocalDate startDate, LocalDate endDate, BigDecimal expectedSum) {
        BigDecimal result = pendingBalanceCalculatorEngine.getPendingAmountByDateRange(transactionCriteriaList, startDate, endDate);
        assertEquals(expectedSum, result);
    }

    static Stream<Arguments> provideTransactionCriteriaWithEndDate() {
        return Stream.of(
                // Case where scheduledDate is equal to endDate and after startDate
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("150.00"), "Description 1", null, LocalDate.of(2023, 5, 31), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("250.00"), "Description 2", null, LocalDate.of(2023, 5, 31), null, null, null, true)
                ), LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 31), new BigDecimal("400.00")),
                // Another case where scheduledDate is equal to endDate and after startDate
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("300.00"), "Description 3", null, LocalDate.of(2023, 6, 30), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("400.00"), "Description 4", null, LocalDate.of(2023, 6, 30), null, null, null, true),
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 5", null, LocalDate.of(2023, 6, 30), null, null, null, true)
                ), LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30), new BigDecimal("800.00"))
        );
    }


    @Test
    public void testGetPendingAmountByDateRange_whenCriteriaIsValid_returnPending(){
        List<TransactionCriteria> transactionCriteria = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

    }



    @ParameterizedTest(name = "{index} => transactionCriteriaList={0}, expectedSum={1}")
    @MethodSource("provideTransactionCriteriaLists")
    @DisplayName("Test getPendingAmountSum with various transaction criteria lists")
    void testGetPendingAmountSum(List<TransactionCriteria> transactionCriteriaList, BigDecimal expectedSum) {
        BigDecimal result = pendingBalanceCalculatorEngine.getPendingAmountSum(transactionCriteriaList);
        assertEquals(expectedSum, result);
    }

    private static Stream<Arguments> provideTransactionCriteriaLists() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("100.00"), "Description 1", null, null, null, null, null, true),
                        new TransactionCriteria(new BigDecimal("200.00"), "Description 2", null, null, null, null, null, true),
                        new TransactionCriteria(new BigDecimal("300.00"), "Description 3", null, null, null, null, null, true)
                ), new BigDecimal("600.00")),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("50.00"), "Description 1", null, null, null, null, null, true),
                        new TransactionCriteria(new BigDecimal("150.00"), "Description 2", null, null, null, null, null, true)
                ), new BigDecimal("200.00")),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(null, "Description 1", null, null, null, null, null, true)
                ), BigDecimal.ZERO),
                Arguments.of(Arrays.asList(
                        new TransactionCriteria(new BigDecimal("0.00"), "Description 1", null, null, null, null, null, true),
                        new TransactionCriteria(new BigDecimal("0.00"), "Description 2", null, null, null, null, null, true)
                ), BigDecimal.ZERO)
        );
    }




    @AfterEach
    void tearDown() {
    }
}