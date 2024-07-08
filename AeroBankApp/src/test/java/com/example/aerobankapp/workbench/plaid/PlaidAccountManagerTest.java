package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.AccountBalance;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidAccountManagerTest {

    @InjectMocks
    private PlaidAccountManager plaidAccountManager;

    @Mock
    private PlaidApi plaidApi;

    @Mock
    private PlaidAccountsService plaidAccountsService;

    @Mock
    private AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plaidAccountManager = new PlaidAccountManager(plaidAccountsService, plaidApi, accountBaseToPlaidAccountConverter);
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID is invalid, then throw exception")
    public void testGetAllAccountsInvalidUserID() {
        assertThrows(InvalidUserIDException.class, () -> {
            plaidAccountManager.getAllAccounts(-1);
        });
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID is valid, user has plaid accounts, then return ListOfAccountsGetResponse")
    public void testGetAllAccountsValidUserID_UserHasPlaidAccounts_thenReturnListOfAccountsGetResponse() throws IOException {
        int userID = 1;

        AccountsGetResponse response1 = new AccountsGetResponse();

        Optional<PlaidAccountsEntity> plaidAccountsEntity = Optional.of(createPlaidAccountEntity());
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(plaidAccountsEntity);

        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken(plaidAccountsEntity.get().getAccessToken());

        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response1));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callSuccessful);

        AccountsGetResponse actualResponse = plaidAccountManager.getAllAccounts(userID);
        assertEquals(response1, actualResponse);
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID is valid, user has plaid account, access token is empty/null, then throw exception")
    public void testGetAllAccounts_UserHasPlaidAccount_AccessTokenIsNullOrEmpty_thenThrowException() throws IOException {
        int userID = 1;

        Optional<PlaidAccountsEntity> plaidAccountsEntity = Optional.of(createPlaidAccountEntityWithoutAccessToken());
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(plaidAccountsEntity);

        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken(plaidAccountsEntity.get().getAccessToken());

        AccountsGetResponse response = new AccountsGetResponse();
        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callSuccessful);

        assertThrows(PlaidAccessTokenNotFoundException.class, () -> {
            plaidAccountManager.getAllAccounts(userID);
        });
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID, user has plaid account, request is null, then throw exception")
    public void testGetAllAccounts_whenUserID_UserHasPlaidAccounts_RequestIsNull_thenThrowException() throws IOException {
        AccountsGetResponse response = new AccountsGetResponse();

        Optional<PlaidAccountsEntity> plaidAccountsEntity = Optional.of(createPlaidAccountEntity());
        when(plaidAccountsService.findPlaidAccountEntityByUserId(1)).thenReturn(plaidAccountsEntity);

        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.accountsGet(null)).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> {
            plaidAccountManager.getAllAccounts(1);
        });
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID valid, user has no plaid accounts, then throw exception")
    public void testGetAllAccounts_whenUserIDValid_UserHasNoPlaidAccounts_thenThrowException() throws IOException {
        when(plaidAccountsService.findPlaidAccountEntityByUserId(1)).thenReturn(Optional.empty());
        assertThrows(PlaidAccountNotFoundException.class, () -> {
            plaidAccountManager.getAllAccounts(1);
        });
    }

    @Test
    @DisplayName("Test GetAllAccounts when userID is valid, user has plaid account, AccountsGetResponse is null, then throw exception")
    public void testGetAllAccounts_whenUserIDValid_UserHasPlaidAccount_AccountsGetResponseIsNull_thenThrowException() throws IOException {

        when(plaidAccountsService.findPlaidAccountEntityByUserId(1)).thenReturn(Optional.of(createPlaidAccountEntity()));
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken(createPlaidAccountEntity().getAccessToken());
        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(null));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callSuccessful);

        assertThrows(PlaidAccountsGetResponseNullPointerException.class, () -> {
            plaidAccountManager.getAllAccounts(1);
        });
    }


    @Test
    @DisplayName("Test GetPlaidAccountsSetFromResponse when list of account base is null, then throw exception")
    public void testGetPlaidAccountsSetFromResponse_whenListOfAccountBaseIsNullOrEmpty_thenThrowException() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountManager.getPlaidAccountsSetFromResponse(null);
        });
    }

    @Test
    @DisplayName("Test GetPlaidAccountsSetFromResponse when list of account base is empty, then throw exception")
    public void testGetPlaidAccountsSetFromResponse_whenListOfAccountBaseIsEmpty_thenThrowException() throws IOException {
        assertThrows(NonEmptyListRequiredException.class, () -> {
            plaidAccountManager.getPlaidAccountsSetFromResponse(Collections.emptyList());
        });
    }

    @Test
    @DisplayName("Test GetPlaidAccountsSetFromResponse when account base list is non empty, then return plaid accounts")
    public void testGetPlaidAccountsSetFromResponse_whenListOfAccountBaseIsNonEmpty_thenReturnPlaidAccounts() throws IOException {
        List<AccountBase> accountBaseList = new ArrayList<>();
        accountBaseList.add(createTestAccountBase());

        Set<PlaidAccount> expectedPlaidAccounts = new HashSet<>();
        expectedPlaidAccounts.add(createTestPlaidAccount());

        Set<PlaidAccount> actualPlaidAccounts = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);

        assertNotNull(actualPlaidAccounts);
        assertEquals(expectedPlaidAccounts, actualPlaidAccounts);
        assertEquals(expectedPlaidAccounts.size(), actualPlaidAccounts.size());
        assertTrue(expectedPlaidAccounts.containsAll(actualPlaidAccounts));
    }

    private AccountBase createTestAccountBase()
    {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAvailable(Double.valueOf(200));
        accountBalance.setCurrent(Double.valueOf(120));

        AccountBase accountBase = new AccountBase();
        accountBase.setAccountId("A1");
        accountBase.setName("Checking");
        accountBase.setVerificationStatus(AccountBase.VerificationStatusEnum.PENDING_AUTOMATIC_VERIFICATION);
        accountBase.setBalances(accountBalance);
        return accountBase;
    }

    private PlaidAccount createTestPlaidAccount() throws IOException {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setVerificationStatus("DONE");
        plaidAccount.setAccountId("A1");
        plaidAccount.setName("Checking");
        plaidAccount.setLimit(BigDecimal.valueOf(100));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        return plaidAccount;
    }


    private PlaidAccountsEntity createPlaidAccountEntity() {
        PlaidAccountsEntity plaidAccountsEntity = new PlaidAccountsEntity();
        plaidAccountsEntity.setAccessToken("access_token");
        plaidAccountsEntity.setItem_id("e1232323");
        plaidAccountsEntity.setUser(UserEntity.builder().userID(1).build());
        plaidAccountsEntity.setInstitution_name("sandbox");
        return plaidAccountsEntity;
    }

    private PlaidAccountsEntity createPlaidAccountEntityWithoutAccessToken(){
        PlaidAccountsEntity plaidAccountsEntity = new PlaidAccountsEntity();
        plaidAccountsEntity.setAccessToken(null);
        plaidAccountsEntity.setItem_id("e1232323");
        plaidAccountsEntity.setUser(UserEntity.builder().userID(1).build());
        plaidAccountsEntity.setInstitution_name("sandbox");
        return plaidAccountsEntity;
    }


    @AfterEach
    void tearDown() {
    }
}