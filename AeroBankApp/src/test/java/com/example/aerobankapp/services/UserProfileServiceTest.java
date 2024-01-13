package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.entity.UserLogEntity;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserProfileServiceTest
{

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private AccountServiceBundle accountServiceBundle;

    @Autowired
    private UserServiceBundle userServiceBundle;

    @Autowired
    private BalanceHistoryDAOImpl balanceHistoryService;

    private CheckingAccountEntity checkingAccount;

    private SavingsAccountEntity savingsAccount;

    @BeforeEach
    void setUp()
    {
        userProfileService = new UserProfileService(userServiceBundle, accountServiceBundle, balanceHistoryService);
        checkingAccount = CheckingAccountEntity.builder()
                .userName("AKing94")
                .minimumBalance(new BigDecimal("100.00"))
                .interestRate(new BigDecimal("1.67"))
                .id(1L)
                .accountName("Checking")
                .balance(new BigDecimal("1250.00"))
                .build();

        savingsAccount = SavingsAccountEntity.builder()
                .accountName("MySavings")
                .balance(new BigDecimal("2500.00"))
                .user("AKing94")
                .dividend_amt(new BigDecimal("0.74"))
                .id("A2")
                .build();
        userProfileService.getAccountServiceBundle().getCheckingService().save(checkingAccount);
        userProfileService.getAccountServiceBundle().getSavingsService().save(savingsAccount);
    }



    @AfterEach
    void tearDown()
    {
        userProfileService.getAccountServiceBundle().getCheckingService().delete(checkingAccount);
    }
}