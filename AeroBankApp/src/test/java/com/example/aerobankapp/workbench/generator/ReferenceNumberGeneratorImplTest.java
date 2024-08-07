package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.model.ReferenceNumber;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ReferenceNumberGeneratorImplTest {

    private ReferenceNumberGenerator referenceNumberGenerator;
    private String acctRefCode = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        referenceNumberGenerator = new ReferenceNumberGeneratorImpl(TransactionType.DEPOSIT, TransactionStatus.PENDING, acctRefCode);
    }

    @Test
    public void testReferenceNumberNotNull(){
        ReferenceNumber refNum = referenceNumberGenerator.generateReferenceNumber();
        assertNotNull(refNum);
    }

    @Test
    public void testReferenceNumberContainsParameters(){
        ReferenceNumber refNum = referenceNumberGenerator.generateReferenceNumber();
        assertEquals(TransactionType.DEPOSIT, refNum.getTransactionType());
        assertEquals(TransactionStatus.PENDING, refNum.getTransactionStatus());
        assertNotNull(refNum.getAccountReferenceCode());
    }

    @Test
    public void testGenerateReferenceNumberUniqueness(){
        ReferenceNumber refNum1 = referenceNumberGenerator.generateReferenceNumber();
        ReferenceNumber refNum2 = referenceNumberGenerator.generateReferenceNumber();
        assertNotEquals(refNum1.getReferenceValue(), refNum2.getReferenceValue());
    }

    @Test
    public void testGenerateReferenceNumberWithNullInputs(){
        assertThrows(IllegalArgumentException.class, () -> {
            new ReferenceNumberGeneratorImpl(null, TransactionStatus.PENDING, acctRefCode);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new ReferenceNumberGeneratorImpl(TransactionType.DEPOSIT, null, acctRefCode);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new ReferenceNumberGeneratorImpl(TransactionType.DEPOSIT, TransactionStatus.PENDING, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new ReferenceNumberGeneratorImpl(TransactionType.DEPOSIT, TransactionStatus.PENDING, "");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new ReferenceNumberGeneratorImpl(null, null, null);
        });
    }

    @Test
    public void testReferenceNumberType(){
        Object result = referenceNumberGenerator.generateReferenceNumber();
        assertNotNull(result);
        assertTrue(result instanceof ReferenceNumber);
    }

    @AfterEach
    void tearDown() {
    }
}