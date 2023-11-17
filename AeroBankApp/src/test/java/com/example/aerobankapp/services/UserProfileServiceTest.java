package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.entity.SavingsAccount;
import com.example.aerobankapp.entity.UserLog;
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
    private BalanceHistoryServiceImpl balanceHistoryService;

    private CheckingAccount checkingAccount;

    private SavingsAccount savingsAccount;

    @BeforeEach
    void setUp()
    {
        userProfileService = new UserProfileService(accountServiceBundle, userServiceBundle, balanceHistoryService);
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
        String user = "AKing94";
        UserLog userLog = UserLog.builder()
                .userID(1)
                .username("AKing94")
                .lastLogin(new Date())
                .build();

        userProfileService.insertUserLog(userLog);
        List<UserLog> userLogs = userProfileService.getUserLogData(user);
        List<UserLog> userLogList = new ArrayList<>();
        userLogList.add(userLog);

        assertNotNull(userLogs);
        assertEquals(userLogList.get(0).getUserID(), userLogs.get(0).getUserID());
        assertEquals(userLogList.get(0).getUsername(), userLogs.get(0).getUsername());
    }

    @AfterEach
    void tearDown()
    {
        userProfileService.getAccountServiceBundle().getCheckingService().delete(checkingAccount);
    }
}