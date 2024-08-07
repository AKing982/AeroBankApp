package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.BillPaymentHistory;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProcessedBillPaymentVerificationTest {

    @Autowired
    private ProcessedBillPaymentVerification processedBillPaymentVerification;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test verify payment history, when payment history is null, then throw exception")
    public void testPaymentHistory_whenPaymentHistoryIsNull_thenThrowException() {
        assertThrows(InvalidProcessedBillPaymentException.class, () -> {
            processedBillPaymentVerification.verifyPaymentHistorySaved(null);
        });
    }

    @Test
    @DisplayName("Test verify payment history when payment criteria is null, then return false")
    public void testPaymentHistory_whenPaymentCriteriaIsNull_thenThrowException() {
        BillPaymentHistory billPaymentHistory = new BillPaymentHistory();
        billPaymentHistory.setPaymentId(1L);
        billPaymentHistory.setProcessed(true);
        billPaymentHistory.setNextPaymentDate(null);
        billPaymentHistory.setLastPaymentDate(null);
        billPaymentHistory.setDateUpdated(null);
        boolean result = processedBillPaymentVerification.verifyPaymentHistorySaved(billPaymentHistory);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test verify payment history when payment criteria is valid, return true")
    public void testPaymentHistory_whenPaymentCriteriaIsValid_thenReturnTrue() {
        BillPaymentHistory billPaymentHistory = new BillPaymentHistory();
        billPaymentHistory.setPaymentId(3L);
        billPaymentHistory.setProcessed(true);
        billPaymentHistory.setNextPaymentDate(LocalDate.of(2024, 7, 15));
        billPaymentHistory.setLastPaymentDate(LocalDate.of(2024, 6, 4));
        billPaymentHistory.setDateUpdated(LocalDate.of(2024, 6, 28));

        boolean result = processedBillPaymentVerification.verifyPaymentHistorySaved(billPaymentHistory);
        assertTrue(result);
    }

    @AfterEach
    void tearDown() {
    }
}