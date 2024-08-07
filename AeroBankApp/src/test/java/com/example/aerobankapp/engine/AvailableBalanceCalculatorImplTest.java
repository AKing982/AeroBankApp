package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.AccountDetails;
import com.example.aerobankapp.model.TransactionCriteria;
import com.example.aerobankapp.scheduler.ScheduleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
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
class AvailableBalanceCalculatorImplTest {

    @Autowired
    private AvailableBalanceCalculatorImpl availableBalanceCalculator;
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
    public void testCalculateAvailableBalance_whenAccountDetailsNull_throwException(){
        List<TransactionCriteria> transactionCriteriaList = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

        assertThrows(NullPointerException.class, () -> {
            availableBalanceCalculator.calculateAvailableBalance(null, transactionCriteriaList);
        });
    }

    @Test
    public void testCalculateAvailableBalance_whenTransactionCriteriaListIsNull_throwException(){
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);

        assertThrows(NullPointerException.class, () -> {
            availableBalanceCalculator.calculateAvailableBalance(accountDetails, null);
        });
    }

    @Test
    public void testCalculateAvailableBalance_whenTransactionCriteriaListEmpty_returnZero(){
        List<TransactionCriteria> emptyList = Collections.emptyList();
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);

        BigDecimal available = availableBalanceCalculator.calculateAvailableBalance(accountDetails, emptyList);

        assertEquals(BigDecimal.ZERO, available);
    }

    @Test
    public void testCalculateAvailableBalance_whenAmountNull_throwException(){
        List<TransactionCriteria> transactionCriteriaList = Arrays.asList(
                new TransactionCriteria(null, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> {
            availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);
        });
    }

    @Test
    public void testCalculateAvailableBalance_whenCurrentBalanceNull_throwException(){
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), null, BigDecimal.ZERO);
        List<TransactionCriteria> transactionCriteriaList = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );

      //  BigDecimal available = availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);

        assertThrows(IllegalArgumentException.class, () -> {
            availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);
        });
    }

    @Test
    public void testCalculateAvailableBalance_whenInputValid_returnAvailable(){
        List<TransactionCriteria> transactionCriteriaList = Arrays.asList(
                new TransactionCriteria(AMOUNT, DESCRIPTION, SCHEDULED_TIME, SCHEDULED_DATE, POSTED, MONTHLY, Currency.getInstance(Locale.US), true)
        );
        AccountDetails accountDetails = new AccountDetails(1, new BigDecimal("1200.00"), new BigDecimal("50.00"), new BigDecimal("1250.00"), BigDecimal.ZERO);

        BigDecimal available = availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);

        assertNotNull(available);
        assertEquals(new BigDecimal("1130.00"), available);
    }

    static Stream<Arguments> provideTransactionData() {
        return Stream.of(
                // Case with valid transactions
                Arguments.of(
                        new AccountDetails(1, new BigDecimal("1000.00"), BigDecimal.ZERO, new BigDecimal("1000.00"), BigDecimal.ZERO),
                        Arrays.asList(
                                new TransactionCriteria(new BigDecimal("100.00"), "Transaction 1", null, LocalDate.now(), null, null, null, true),
                                new TransactionCriteria(new BigDecimal("200.00"), "Transaction 2", null, LocalDate.now(), null, null, null, true)
                        ),
                        new BigDecimal("700.00")
                ),
                // Case with a transaction having null amount
                Arguments.of(
                        new AccountDetails(1, new BigDecimal("1000.00"), BigDecimal.ZERO, new BigDecimal("1000.00"), BigDecimal.ZERO),
                        Arrays.asList(
                                new TransactionCriteria(null, "Transaction 1", null, LocalDate.now(), null, null, null, true),
                                new TransactionCriteria(new BigDecimal("200.00"), "Transaction 2", null, LocalDate.now(), null, null, null, true)
                        ),
                        IllegalArgumentException.class
                ),
                // Case with a transaction having null scheduled date
                Arguments.of(
                        new AccountDetails(1, new BigDecimal("1000.00"), BigDecimal.ZERO, new BigDecimal("1000.00"), BigDecimal.ZERO),
                        Arrays.asList(
                                new TransactionCriteria(new BigDecimal("100.00"), "Transaction 1", null, null, null, null, null, true),
                                new TransactionCriteria(new BigDecimal("200.00"), "Transaction 2", null, LocalDate.now(), null, null, null, true)
                        ),
                        IllegalArgumentException.class
                ),
                // Case with an empty transaction list
                Arguments.of(
                        new AccountDetails(1, new BigDecimal("1000.00"), BigDecimal.ZERO, new BigDecimal("1000.00"), BigDecimal.ZERO),
                        Collections.emptyList(),
                        new BigDecimal("1000.00")
                )
        );
    }
    @ParameterizedTest
    @MethodSource("provideTransactionData")
    @DisplayName("Test calculateAvailableBalance with various transaction data")
    void testCalculateAvailableBalance(AccountDetails accountDetails, List<TransactionCriteria> transactionCriteriaList, Object expectedResult) {
        if (expectedResult instanceof BigDecimal) {
            BigDecimal result = availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);
            assertEquals(expectedResult, result);
        } else if (expectedResult instanceof Class) {
            assertThrows((Class<? extends Throwable>) expectedResult, () -> {
                availableBalanceCalculator.calculateAvailableBalance(accountDetails, transactionCriteriaList);
            });
        }
    }

    @AfterEach
    void tearDown() {
    }
}