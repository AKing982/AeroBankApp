package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.LinkedAccountInfoListNullException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.PlaidAccountsGetResponseNullPointerException;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.ExternalAccountsRepository;
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
import static org.mockito.Mockito.*;

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

    @Mock
    private ExternalAccountsRepository externalAccountsRepository;

    @BeforeEach
    void setUp() {
        plaidAccountImportRunner = new PlaidAccountImportRunner(plaidAccountManager, plaidAccountImporter, userRepository, accountRepository, externalAccountsRepository);
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

    @Test
    public void testImportPlaidAccounts_whenLinkedAccountInfoListIsNull_thenThrowException() throws IOException, InterruptedException {

        int userId = 1;
        UserEntity userEntity = createUserEntity(userId);
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        List<AccountBase> accountBaseList = accountsGetResponse.getAccounts();

        when(plaidAccountManager.getAllAccounts(userId)).thenReturn(accountsGetResponse);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(plaidAccountImporter.prepareLinkedAccounts(any(UserEntity.class), anyList())).thenReturn(null);

        assertThrows(LinkedAccountInfoListNullException.class, () -> {
            plaidAccountImportRunner.importPlaidAccounts(userId);
        });

        verify(plaidAccountManager, times(1))
                .getAllAccounts(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(plaidAccountImporter, times(1))
                .prepareLinkedAccounts(any(UserEntity.class), anyList());

    }

    @Test
    public void testImportPlaidAccounts_whenLinkedAccountInfoListIsEmpty_thenThrowException() throws IOException, InterruptedException {
        int userId = 1;
        UserEntity userEntity = createUserEntity(userId);
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();

        when(plaidAccountManager.getAllAccounts(userId)).thenReturn(accountsGetResponse);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(plaidAccountImporter.prepareLinkedAccounts(any(UserEntity.class), anyList())).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            plaidAccountImportRunner.importPlaidAccounts(userId);
        });

        verify(plaidAccountManager, times(1))
                .getAllAccounts(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(plaidAccountImporter, times(1))
                .prepareLinkedAccounts(any(UserEntity.class), anyList());

    }

    @Test
    public void testImportPlaidAccounts_whenCreateAndSaveStepIsTrue_thenImportPlaidAccounts() throws IOException, InterruptedException {
        int userId = 1;
        UserEntity userEntity = createUserEntity(userId);
        PlaidAccount plaidAccount = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        List<PlaidAccount> plaidAccountList = new ArrayList<>();
        plaidAccountList.add(plaidAccount);

        List<LinkedAccountInfo> linkedAccountInfoList = new ArrayList<>();
        LinkedAccountInfo linkedAccountInfo = mock(LinkedAccountInfo.class);
        linkedAccountInfoList.add(linkedAccountInfo);

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();

        when(plaidAccountManager.getAllAccounts(userId)).thenReturn(accountsGetResponse);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(plaidAccountImporter.prepareLinkedAccounts(any(UserEntity.class), anyList())).thenReturn(linkedAccountInfoList);
        when(plaidAccountImporter.executeCreateAndSaveExternalAccountEntity(linkedAccountInfoList)).thenReturn(true);

        plaidAccountImportRunner.importPlaidAccounts(userId);

        verify(plaidAccountManager, times(1))
                .getAllAccounts(anyInt());
        verify(userRepository, times(1)).findById(userId);
        verify(plaidAccountImporter, times(1))
                .prepareLinkedAccounts(any(UserEntity.class), anyList());
        verify(plaidAccountImporter, times(1))
                .executeCreateAndSaveExternalAccountEntity(linkedAccountInfoList);
    }

    @Test
    public void testCreateAndSaveExternalAccount() throws IOException, InterruptedException {
        List<LinkedAccountInfo> linkedAccountInfoList = new ArrayList<>();
        LinkedAccountInfo linkedAccountInfo = createLinkedAccountInfo("e2323232", 1);
        LinkedAccountInfo linkedAccountInfo1 = createLinkedAccountInfo("f3432432", 2);
        linkedAccountInfoList.add(linkedAccountInfo);
        linkedAccountInfoList.add(linkedAccountInfo1);

        Boolean result = plaidAccountImportRunner.createAndSaveExternalAccount(linkedAccountInfoList);
        assertTrue(result);
    }


     @AfterEach
    void tearDown() {
    }

    private UserEntity createUserEntity(int userID)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(userID);
        return userEntity;
    }

    private LinkedAccountInfo createLinkedAccountInfo(String external, int acctID)
    {
        LinkedAccountInfo linkedAccountInfo = new LinkedAccountInfo();
        linkedAccountInfo.setExternalAcctID(external);
        linkedAccountInfo.setSystemAcctID(acctID);
        return linkedAccountInfo;
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