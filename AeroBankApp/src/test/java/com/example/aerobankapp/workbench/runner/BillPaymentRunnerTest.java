package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.BillPaymentEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentRunnerTest {

    @Autowired
    private BillPaymentRunner billPaymentRunner;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test GetAllBillPayments when result is null, return empty collection")
    public void testGetAllBillPayments_whenresultIsNull_returnEmptyCollection() {
        Collection<BillPaymentEntity> billPaymentEntities = billPaymentRunner.getAllBillPayments();
        assertEquals(0, billPaymentEntities.size());
    }

    @AfterEach
    void tearDown() {
    }
}