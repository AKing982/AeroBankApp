package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit38ClassRunner.class)
@SpringBootTest
class AccountDAOImplTest
{
    @MockBean
    private AccountDAOImpl accountDAO;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private AccountEntity accountEntity;


    @BeforeEach
    void setUp()
    {
        accountDAO = new AccountDAOImpl(entityManager, accountRepository);
        accountEntity = AccountEntity.builder()
                .accountName("AKing Checking")
                .accountType("Checking")
                .isRentAccount(false)
                .acctID(1)
                .accountCode("A1")
                .hasDividend(false)
                .balance(new BigDecimal("1205"))
                .interest(new BigDecimal("0.00"))
                .userID(1)
                .hasMortgage(false)
                .build();
    }

    @Test
    public void saveAccount()
    {
        accountDAO.delete(accountEntity);
        accountDAO.save(accountEntity);
        List<AccountEntity> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(accountEntity);

        List<AccountEntity> accountEntityList = accountDAO.findAll();

        assertNotNull(accountEntity);
        assertEquals(1, accountEntityList.size());
        assertEquals(expectedAccounts.size(), accountEntityList.size());
        assertEquals(expectedAccounts.get(0).getAccountCode(), accountEntityList.get(0).getAccountCode());
    }

    @Test
    public void testGetValidUserName()
    {
        String username = "AKing94";
        List<AccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(accountEntity);

        List<AccountEntity> akingAccounts = accountDAO.findByUserName(username);
        System.out.println(akingAccounts.stream().findFirst().isPresent());
      //  int expectedUserID = Objects.requireNonNull(accountEntities.stream().findFirst().orElse(null)).getUserID();
       // int actualID = Objects.requireNonNull(akingAccounts.stream().findFirst().orElse(null)).getUserID();

        assertEquals(accountEntities.size(), akingAccounts.size());
       // assertEquals(expectedUserID, actualID);

    }

    @AfterEach
    void tearDown() {
    }
}