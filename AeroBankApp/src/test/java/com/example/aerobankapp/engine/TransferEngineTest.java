package com.example.aerobankapp.engine;

import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransferEngineTest {

    @InjectMocks
    private TransferEngine transferEngine;

    @MockBean
    private TransferService transferService;

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
        transferEngine = new TransferEngine(transferService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
    }



    @AfterEach
    void tearDown() {
    }
}