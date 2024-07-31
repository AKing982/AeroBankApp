package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.PlaidAccountsGetResponseNullPointerException;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.plaid.PlaidAccountImporter;
import com.example.aerobankapp.workbench.plaid.PlaidAccountManager;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({JpaConfig.class, AppConfig.class})
class PlaidAccountImportRunnerTest {

    @InjectMocks
    private PlaidAccountImportRunner plaidAccountImportRunner;

    @Mock
    private PlaidAccountManager plaidAccountManager;

    @Mock
    private PlaidAccountImporter plaidAccountImporter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        plaidAccountImportRunner = new PlaidAccountImportRunner(plaidAccountManager, plaidAccountImporter, userRepository, accountRepository);
    }

    @Test
    @DisplayName("Test GetUserPlaidAccounts when userId invalid, then throw exception")
    public void testGetUserPlaidAccounts_whenUserIdInvalid_thenThrowException(){
        final int INVALID_USERID = -1;

        assertThrows(InvalidUserIDException.class, () -> {
            plaidAccountImportRunner.getUserPlaidAccounts(INVALID_USERID);
        });
    }

    @Test
    public void testGetUserPlaidAccounts_whenUserIdValid_thenReturnPlaidAccounts() throws IOException, InterruptedException {
        final int VALID_USERID = 1;
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        Set<PlaidAccount> plaidAccountSet = new HashSet<>();
        plaidAccountSet.add(plaidAccount);

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        List<AccountBase> accountBaseList = accountsGetResponse.getAccounts();

        when(plaidAccountManager.getAllAccounts(VALID_USERID)).thenReturn(accountsGetResponse);
        when(plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList)).thenReturn(plaidAccountSet);

        List<PlaidAccount> actual = plaidAccountImportRunner.getUserPlaidAccounts(VALID_USERID);

        assertNotNull(actual);
        assertEquals(plaidAccountList.size(), actual.size());
    }

    @Test
    @DisplayName("Test Get User Plaid Accounts when UserId Valid and AccountsGetResponse is null, then throw exception")
    public void testGetUserPlaidAccounts_whenUserIdValid_AccountsGetResponseIsNull_thenThrowException() throws IOException, InterruptedException {
        final int VALID_USERID = 1;
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        when(plaidAccountManager.getAllAccounts(VALID_USERID)).thenReturn(null);

        assertThrows(PlaidAccountsGetResponseNullPointerException.class, () -> {
            plaidAccountImportRunner.getUserPlaidAccounts(VALID_USERID);
        });
    }

    @Test
    public void testGetUserPlaidAccounts_whenUserIdValid_AccountBaseListIsEmpty_thenThrowException() throws IOException, InterruptedException {
        final int VALID_USERID = 1;
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        Set<PlaidAccount> plaidAccountSet = new HashSet<>();
        plaidAccountSet.add(plaidAccount);

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        accountsGetResponse.setAccounts(Collections.emptyList());

        when(plaidAccountManager.getAllAccounts(VALID_USERID)).thenReturn(accountsGetResponse);

        assertThrows(NonEmptyListRequiredException.class, () -> {
            plaidAccountImportRunner.getUserPlaidAccounts(VALID_USERID);
        });
    }



     @AfterEach
    void tearDown() {
    }

    private PlaidAccount createPlaidAccount(String name, String type, String subtype, String acctID)
    {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId(acctID);
        plaidAccount.setName(name);
        plaidAccount.setSubtype(subtype);
        plaidAccount.setType(type);
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        return plaidAccount;
    }

    private PlaidAccount createPlaidAccountWithMask(String name, String type, String subtype, String acctID, String mask){
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId(acctID);
        plaidAccount.setName(name);
        plaidAccount.setSubtype(subtype);
        plaidAccount.setType(type);
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        plaidAccount.setMask(mask);
        return plaidAccount;
    }



}