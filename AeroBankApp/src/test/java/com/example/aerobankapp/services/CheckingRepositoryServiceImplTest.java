package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
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
    private EntityManager entityManager;

    @Autowired
    private CheckingRepository checkingRepository;
    private CheckingAccount checkingAccount;

    @BeforeEach
    void setUp()
    {
        checkingAccount = CheckingAccount.builder()
                .accountName("Checking")
                .balance(new BigDecimal("1200.00"))
                .interestRate(new BigDecimal("2.67"))
                .minimumBalance(new BigDecimal("100.00"))
                .userName("AKing94")
                .id("A1")
                .build();

        checkingRepositoryService = new CheckingRepositoryServiceImpl(checkingRepository, entityManager);

    }

    @Test
    public void saveCheckingAccount()
    {
        checkingRepositoryService.save(checkingAccount);
        List<CheckingAccount> accounts = checkingRepositoryService.findAll();

        assertNotNull(accounts);
        assertEquals(checkingAccount, accounts.get(0));
    }

    @Test
    public void deleteChecking()
    {
        checkingRepositoryService.delete(checkingAccount);
        List<CheckingAccount> accounts = checkingRepositoryService.findAll();

        assertNotNull(accounts);
        assertTrue(accounts.size() == 0);
    }

    @Test
    public void findByUserName()
    {
        checkingRepositoryService.save(checkingAccount);
        List<CheckingAccount> checkingAccount1 = checkingRepositoryService.findByUserName("AKing94");

        assertEquals(checkingAccount, checkingAccount1.get(0));
    }


    @AfterEach
    void tearDown() {
    }
}