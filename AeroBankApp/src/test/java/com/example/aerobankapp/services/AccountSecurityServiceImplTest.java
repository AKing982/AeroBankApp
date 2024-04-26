package com.example.aerobankapp.services;

import com.example.aerobankapp.repositories.AccountSecurityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class AccountSecurityServiceImplTest
{
    @InjectMocks
    private AccountSecurityServiceImpl accountSecurityService;

    @Autowired
    private AccountSecurityRepository accountSecurityRepository;

    @BeforeEach
    void setUp() {
       // accountSecurityService = new AccountSecurityServiceImpl(accountSecurityRepository);
    }

    @Test
    public void testGetMinimumBalance_ValidAcctID(){
        final int acctID = 1;

        BigDecimal expectedMinimumBalance = new BigDecimal("120.000");
        BigDecimal actualMinimumBalance = accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);

        assertEquals(expectedMinimumBalance, actualMinimumBalance);
    }

    @AfterEach
    void tearDown() {
    }
}