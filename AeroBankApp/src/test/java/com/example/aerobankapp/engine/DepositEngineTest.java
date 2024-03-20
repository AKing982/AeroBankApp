package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Deposit;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositEngineTest {

    @InjectMocks
    private DepositEngine engine;

    @Autowired
    private DepositService depositService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSecurityService accountSecurityService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private BalanceHistoryService balanceHistoryService;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeEach
    void setUp() {
        engine = new DepositEngine(depositService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);

    }

    private static Stream<List<Deposit>> depositProvider() {
        // Provide different lists of deposits to simulate scenarios
        return Stream.of(
                List.of(/* Scenario 1 deposits */),
                List.of(/* Scenario 2 deposits */)
                // Add as many scenarios as needed
        );
    }

    @ParameterizedTest
    @MethodSource("depositProvider")
    public void runTest(List<Deposit> deposits) {
        // Setup mocks based on the provided deposits
       // when(depositService.findAll()).thenReturn(deposits);
        // Mock other service behaviors based on scenario

        // Execute the run method
        engine.run();

        // Verify interactions and assert conditions based on the scenario
        // For example, verify if notifications were sent, balances were updated, etc.
    }

    @AfterEach
    void tearDown() {
    }
}