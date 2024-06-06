package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProcessedBillPaymentVerificationTest {

    @MockBean
    private ProcessedBillPaymentVerification processedBillPaymentVerification;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test verify payment history, when payment history is null, then throw exception")
    public void testPaymentHistory_whenPaymentHistoryIsNull_thenThrowException() {
        assertThrows(InvalidProcessedBillPaymentException.class, () -> {
            processedBillPaymentVerification.verifyPaymentHistory(null);
        });
    }

    @AfterEach
    void tearDown() {
    }
}