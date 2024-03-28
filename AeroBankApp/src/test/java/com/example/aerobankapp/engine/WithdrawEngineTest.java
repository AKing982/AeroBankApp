package com.example.aerobankapp.engine;

import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class WithdrawEngineTest {

    @InjectMocks
    private WithdrawEngine withdrawEngine;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

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
        withdrawEngine = new WithdrawEngine(withdrawService, userService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
    }

    @Test
    public void testFetchAllMethod(){
        List<Withdraw> fetchedWithdrawals = withdrawEngine.fetchAll();

        assertNotNull(fetchedWithdrawals);
        assertEquals(1, fetchedWithdrawals.size());
    }

    @AfterEach
    void tearDown() {
    }
}