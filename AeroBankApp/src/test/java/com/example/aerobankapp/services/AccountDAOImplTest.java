package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDAOImplTest
{
    @InjectMocks
    private AccountServiceImpl accountDAO;

    @Mock
    private TypedQuery<Integer> typedQuery;

    @Mock
    private EntityManager entityManager;

    private AccountEntity accountEntity;


    @BeforeEach
    void setUp()
    {
        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(typedQuery);
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

      //  assertEquals(accountEntities.size(), akingAccounts.size());
        assertEquals(3, akingAccounts.size());
       // assertEquals(expectedUserID, actualID);

    }

    @Test
    public void testValidUserNameBSmith23()
    {
        String username = "BSmith23";
        List<AccountEntity> accountEntities = accountDAO.findByUserName(username);

        assertEquals(1, accountEntities.size());
    }

    @Test
    void getNumberOfAccounts_ValidUsername() {
        String username = "AKing94";
        int expectedCount = 5;

        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedCount);

        Long actualCount = accountDAO.getNumberOfAccounts(username);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getNumberOfAccounts_NoUserFound() {
        String username = "NonExistentUser";

        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0); // Assuming 0 is returned when no user is found

        Long actualCount = accountDAO.getNumberOfAccounts(username);

        assertEquals(0, actualCount);
    }

    @AfterEach
    void tearDown() {
    }
}