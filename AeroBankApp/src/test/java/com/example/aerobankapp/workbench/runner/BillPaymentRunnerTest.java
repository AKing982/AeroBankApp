package com.example.aerobankapp.workbench.runner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentRunnerTest {

    @Autowired
    private BillPaymentRunner billPaymentRunner;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}