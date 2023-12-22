package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.repositories.CheckingRepository;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class CheckingRepositoryServiceImplTest
{
    @MockBean
    private CheckingRepositoryServiceImpl checkingRepositoryService;

    @Autowired
    private CheckingRepository checkingRepository;

    @Autowired
    private EntityManager entityManager;

    private CheckingAccountEntity checkingAccount;

    @BeforeEach
    void setUp()
    {

        checkingRepositoryService = new CheckingRepositoryServiceImpl(checkingRepository, entityManager);

    }

    @Test
    public void saveCheckingAccount()
    {

        CheckingAccountEntity checkingAccount = CheckingAccountEntity.builder()
                .accountName("Checking")
                .balance(new BigDecimal("1200.00"))
                .interestRate(new BigDecimal("2.67"))
                .minimumBalance(new BigDecimal("100.00"))
                .userName("AKing94")
                .build();

        checkingRepositoryService.save(checkingAccount);
        List<CheckingAccountEntity> accounts = checkingRepositoryService.findAll();

        assertNotNull(accounts);
        assertNotEquals(checkingAccount, accounts.get(0));
    }

    @Test
    public void deleteChecking()
    {
        CheckingAccountEntity checkingAccount = CheckingAccountEntity.builder()
                .accountName("Checking")
                .balance(new BigDecimal("1200.00"))
                .interestRate(new BigDecimal("2.67"))
                .minimumBalance(new BigDecimal("100.00"))
                .userName("AKing94")
                .id(2L)
                .build();

        checkingRepositoryService.delete(checkingAccount);
        List<CheckingAccountEntity> accounts = checkingRepositoryService.findAll();

        assertNotNull(accounts);
        assertTrue(accounts.size() == 0);
    }

    @Test
    public void findByUserName()
    {
        CheckingAccountEntity checkingAccount = CheckingAccountEntity.builder()
                .accountName("Checking")
                .balance(new BigDecimal("1200.00"))
                .interestRate(new BigDecimal("2.67"))
                .minimumBalance(new BigDecimal("100.00"))
                .userName("AKing94")
                .id(2L)
                .build();

        checkingRepositoryService.save(checkingAccount);
        List<CheckingAccountEntity> checkingAccount1 = checkingRepositoryService.findByUserName("AKing94");

        assertNotEquals(checkingAccount, checkingAccount1.get(0));
    }


    @AfterEach
    void tearDown() {
    }
}