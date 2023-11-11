package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.entity.SavingsAccount;
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
class UserProfileServiceTest
{

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private AccountServiceBundle accountServiceBundle;

    private CheckingAccount checkingAccount;

    private SavingsAccount savingsAccount;

    @BeforeEach
    void setUp()
    {
        userProfileService = new UserProfileService(accountServiceBundle);
        checkingAccount = CheckingAccount.builder()
                .userName("AKing94")
                .minimumBalance(new BigDecimal("100.00"))
                .interestRate(new BigDecimal("1.67"))
                .id("A1")
                .accountName("Checking")
                .balance(new BigDecimal("1250.00"))
                .build();

        savingsAccount = SavingsAccount.builder()
                .accountName("MySavings")
                .balance(new BigDecimal("2500.00"))
                .user("AKing94")
                .dividend_amt(new BigDecimal("0.74"))
                .id("A2")
                .build();
        userProfileService.getAccountServiceBundle().getCheckingService().save(checkingAccount);
        userProfileService.getAccountServiceBundle().getSavingsService().save(savingsAccount);
    }

    @Test
    public void getCheckingAccountsFromUserAKing94()
    {
        String user = "AKing94";
        long uID = 1;
        String acctID = "A1";
        String acctName = "Checking";

        List<CheckingAccount> userChecking = userProfileService.getCheckingAccounts(user);
        assertNotNull(userChecking);
        assertTrue(userChecking.size() >= 1);
        assertEquals(checkingAccount, userChecking.get(0));
        assertEquals(user, userChecking.get(0).getUserName());
        assertEquals(acctID, userChecking.get(0).getId());
    }

    @Test
    public void testSavingsAccounts()
    {
        String user = "AKing94";
        List<SavingsAccount> savingsAccounts = userProfileService.getSavingsAccounts(user);
        assertNotNull(savingsAccounts);
        assertEquals(savingsAccount.getUser(), savingsAccounts.get(0).getUser());
        assertEquals(savingsAccount.getAccountName(), savingsAccounts.get(0).getAccountName());
    }

    @Test
    public void testUserLogData()
    {

    }

    @AfterEach
    void tearDown()
    {
        userProfileService.getAccountServiceBundle().getCheckingService().delete(checkingAccount);
    }
}