package com.example.aerobankapp.services;

import com.example.aerobankapp.scheduler.DepositScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositSubmitterImplTest {

    @InjectMocks
    private DepositSubmitterImpl depositSubmitter;

    @Mock
    private DepositService depositService;

    @Mock
    private DepositScheduler depositScheduler;

    @Mock
    private FinancialEncryptionService financialEncryptionService;

    @BeforeEach
    void setUp() {
        depositSubmitter = new DepositSubmitterImpl(depositService, depositScheduler, financialEncryptionService);
    }



    @AfterEach
    void tearDown() {
    }
}