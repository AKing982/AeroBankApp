package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.CheckingAccountEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AccountServiceBundleTest
{
    @MockBean
    private AccountServiceBundle serviceBundle;
    @Autowired
    private CheckingRepositoryServiceDAOImpl checkingRepositoryService;
    @Autowired
    private SavingsAccountDAOImpl savingsAccountService;

    @Autowired
    private InvestmentAccountDAOImpl investmentAccountDAO;
    private CheckingAccountEntity checkingAccount;

    @BeforeEach
    void setUp()
    {
       // serviceBundle = new AccountServiceBundle(checkingRepositoryService, savingsAccountService, investmentAccountDAO);
        checkingAccount = CheckingAccountEntity.builder()
                .userName("AKing94")
                .minimumBalance(new BigDecimal("100.00"))
                .interestRate(new BigDecimal("1.67"))
                .id(1L)
                .accountName("Checking")
                .balance(new BigDecimal("1250.00"))
                .build();
    }



    @Test
    public void testFindById()
    {

    }

    @AfterEach
    void tearDown() {
    }
}