package com.example.aerobankapp.engine;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class PendingBalanceCalculatorEngineImplTest {

    @InjectMocks
    private PendingBalanceCalculatorEngineImpl pendingBalanceCalculatorEngine;

    @BeforeEach
    void setUp() {
        pendingBalanceCalculatorEngine = new PendingBalanceCalculatorEngineImpl();
    }



    @AfterEach
    void tearDown() {
    }
}