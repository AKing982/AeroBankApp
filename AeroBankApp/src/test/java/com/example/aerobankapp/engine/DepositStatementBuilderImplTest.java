package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.services.PendingTransactionsService;
import com.example.aerobankapp.services.TransactionService;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositStatementBuilderImplTest {

    @InjectMocks
    private DepositStatementBuilderImpl depositStatementBuilder;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PendingTransactionsService pendingTransactionsService;

    @BeforeEach
    void setUp() {
        depositStatementBuilder = new DepositStatementBuilderImpl(transactionService, pendingTransactionsService);

    }

    @Test
    public void testGetFormattedDate_InvalidNullDate(){
        LocalDateTime now = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class, () -> {
            depositStatementBuilder.getFormattedDate(null);
        });
    }

    @Test
    public void testGetFormattedDate_ValidDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

        String expectedFormattedDate = now.format(formatter);
        String actualFormattedDate = depositStatementBuilder.getFormattedDate(now);

        assertEquals(now, actualFormattedDate);
        assertEquals(expectedFormattedDate, actualFormattedDate);
    }

    @Test
    public void testGetFormattedDate_SpecificKnownDate(){
        LocalDateTime specificDate = LocalDateTime.of(2024, 2, 4, 0, 0); // February 4, 2024
        String expected = "February 4, 2024";
        String actual = depositStatementBuilder.getFormattedDate(specificDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFormattedDate_EndOfMonth() {
        LocalDateTime endOfMonth = LocalDateTime.of(2023, 1, 31, 23, 59);
        String expected = "January 31, 2023";
        String actual = depositStatementBuilder.getFormattedDate(endOfMonth);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFormattedDate_LeapYear() {
        LocalDateTime leapYearDate = LocalDateTime.of(2024, 2, 29, 0, 0); // Leap year date
        String expected = "February 29, 2024";
        String actual = depositStatementBuilder.getFormattedDate(leapYearDate);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFormattedDate_TimeComponentIgnored() {
        LocalDateTime midday = LocalDateTime.of(2023, 3, 15, 12, 0); // March 15, 2023, at 12:00
        LocalDateTime midnight = midday.withHour(0); // March 15, 2023, at 00:00
        String expected = "March 15, 2023";
        assertEquals(depositStatementBuilder.getFormattedDate(midday), depositStatementBuilder.getFormattedDate(midnight));
    }

    @Test
    public void testGetFormattedAmount_NullAmount(){

        assertThrows(IllegalArgumentException.class, () -> {
            String formattedAmount = depositStatementBuilder.getFormattedAmount(null);
        });
    }

    @Test
    public void testGetFormattedAmount_EmptyAmount(){

        assertThrows(NumberFormatException.class, () -> {
            depositStatementBuilder.getFormattedAmount(new BigDecimal(""));
        });
    }

    @Test
    public void testGetFormattedAmount_ValidAmount(){
        BigDecimal amount = new BigDecimal("45.00");
        String expectedFormattedAmount = "$45.00";

        String actualFormattedAmount = depositStatementBuilder.getFormattedAmount(amount);

        assertEquals(expectedFormattedAmount, actualFormattedAmount);
    }

    @Test
    public void testRetrieveTransactionsByType_EmptyTransactionEntities(){
        assertThrows(NonEmptyListRequiredException.class, () -> {
            depositStatementBuilder.retrieveTransactionsByType(new ArrayList<>());
        });
    }

    @AfterEach
    void tearDown() {
    }
}