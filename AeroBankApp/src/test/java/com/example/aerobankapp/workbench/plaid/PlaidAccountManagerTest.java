package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.AccountBalance;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.request.PlaidApi;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
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
import org.springframework.retry.RetryPolicy;
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
        plaidAccountManager = new PlaidAccountManager(plaidAccountsService, plaidApi);
        accountBaseToPlaidAccountConverter = mock(AccountBaseToPlaidAccountConverter.class);
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
    public void testGetAllAccountsValidUserID_UserHasPlaidAccounts_thenReturnListOfAccountsGetResponse() throws IOException, InterruptedException {
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

        List<PlaidAccount> expected = expectedPlaidAccounts.stream().toList();

        Set<PlaidAccount> actualPlaidAccounts = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);
        List<PlaidAccount> actual = actualPlaidAccounts.stream().toList();

        assertNotNull(actualPlaidAccounts);
        for(int i = 0; i < actual.size(); i++)
        {
            assertEquals(expected.get(i).getAccountId(), actual.get(i).getAccountId());
            assertEquals(expected.get(i).getMask(), actual.get(i).getMask());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
            assertEquals(expected.get(i).getCurrentBalance(), actual.get(i).getCurrentBalance());
            assertEquals(expected.get(i).getAvailableBalance(), actual.get(i).getAvailableBalance());
        }
        assertEquals(expectedPlaidAccounts.size(), actualPlaidAccounts.size());
    }

    @Test
    @DisplayName("Test GetPlaidAccountsSetFromResponse when account base element is null, skip the null account base and return the set")
    public void testGetPlaidAccountsSetFromResponse_whenAccountBaseElementIsNull_SkipTheNullElement_thenReturnSet() throws IOException {
        List<AccountBase> accountBaseList = new ArrayList<>();
        accountBaseList.add(null);
        accountBaseList.add(createTestAccountBase());

        Set<PlaidAccount> expectedPlaidAccounts = new HashSet<>();
        expectedPlaidAccounts.add(createTestPlaidAccount());

        Set<PlaidAccount> actualPlaidAccounts = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);

        assertNotNull(actualPlaidAccounts);
        assertEquals(expectedPlaidAccounts.size(), actualPlaidAccounts.size());
    }


    @Test
    @DisplayName("Test GetPlaidAccountsSetFromResponse when converted plaid account is null, then skip null plaid account and return set")
    public void testGetPlaidAccountsSetFromResponse_whenConvertedPlaidAccountIsNull_ThenSkipNullPlaidAccount_returnSet() throws IOException {
        List<AccountBase> accountBaseList = new ArrayList<>();
        AccountBase accountBase = createTestAccountBase();
        accountBaseList.add(createTestAccountBase());
        accountBaseList.add(createTestAccountBase());

        PlaidAccount plaidAccount = createTestPlaidAccount();

        when(accountBaseToPlaidAccountConverter.convert(accountBase)).thenReturn(null);
        when(accountBaseToPlaidAccountConverter.convert(accountBase)).thenReturn(plaidAccount);

        Set<PlaidAccount> actual = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);
        assertNotNull(actual);
        assertEquals(1, actual.size());

        PlaidAccount actualPlaidAccount = actual.iterator().next();
        assertNotEquals(plaidAccount, actualPlaidAccount);
    }

    @Test
    @DisplayName("Test getAllAccountsRetryResponse when AccountsGetRequest is null, then throw exception")
    public void testGetAllAccountsRetryResponse_whenAccountsGetRequestIsNull_ThenThrowException() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            plaidAccountManager.getAllAccountsRetryResponse(null);
        });
    }

    @Test
    @DisplayName("Test getAllAccountsRetryResponse when AccountsGetRequest is valid, then return AccountsGetResponse")
    public void testGetAllAccountsRetryResponse_whenAccountsGetRequestIsValid_thenReturnAccountsGetResponse() throws IOException, InterruptedException {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
        accountsGetRequest.accessToken("e2e23232");

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(accountsGetResponse));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callSuccessful);

        AccountsGetResponse actual = plaidAccountManager.getAllAccountsRetryResponse(accountsGetRequest);
        assertNotNull(actual);
        assertEquals(accountsGetResponse, actual);
    }

    @Test
    @DisplayName("Test getAllAccountsRetryResponse when first response fails, succeeds on second pass, then return response")
    public void testGetAllAccountsRetryResponse_whenFirstResponseFails_SucceedsOnSecondPass_thenReturnResponse() throws IOException, InterruptedException {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken("e2e23232");

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        Call<AccountsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(accountsGetResponse));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callUnsuccessful, callSuccessful);

        AccountsGetResponse actual = plaidAccountManager.getAllAccountsRetryResponse(accountsGetRequest);
        assertNotNull(actual);
        assertEquals(accountsGetResponse, actual);
    }

    @Test
    @DisplayName("Test getAllAccountsRetryResponse when max retries reached, then break out and throw exception")
    public void testGetAllAccountsRetryResponse_whenMaxRetriesReached_ThenBreakOutAndThrowException() throws IOException {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken("e2e23232");

        Call<AccountsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenThrow(PlaidAccountsGetResponseNullPointerException.class);

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callUnsuccessful, callUnsuccessful, callUnsuccessful, callUnsuccessful, callUnsuccessful);

        assertThrows(PlaidAccountsGetResponseNullPointerException.class, () -> {
            plaidAccountManager.getAllAccountsRetryResponse(accountsGetRequest);
        });
    }

    @Test
    @DisplayName("Test getAllAccountsRetryResponse when exception is thrown, then throw exception")
    public void testGetAllAccountsRetryResponse_whenExceptionIsThrown_ThenThrowException() throws IOException {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken("e2e23232");

        Call<AccountsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenThrow(new IOException());

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callUnsuccessful);

        assertThrows(IOException.class, () -> {
            plaidAccountManager.getAllAccountsRetryResponse(accountsGetRequest);
        });
    }

    @Test
    @DisplayName("Test getBalancesByUserId when userID is invalid, then throw exception")
    public void testGetBalancesByUserID_whenUserIDIsInvalid_ThenThrowException() throws IOException {
        assertThrows(InvalidUserIDException.class, () -> {
            plaidAccountManager.getBalancesByUserId(-1);
        });
    }

    @Test
    @DisplayName("Test getBalancesByUserId when UserId is valid, user has plaid account, then return")
    public void testGetBalancesByUserId_whenUserIDIsValid_thenReturnAccountsGetResponse() throws IOException, InterruptedException {
        final int userID = 1;

        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(Optional.of(createPlaidAccountEntity()));
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest()
                .accessToken(createPlaidAccountEntity().getAccessToken());

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();

        List<PlaidAccountBalances> expectedPlaidAccountBalancesList = new ArrayList<>();
        expectedPlaidAccountBalancesList.add(createTestPlaidAccountBalances());

        Call<AccountsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(accountsGetResponse));

        when(plaidApi.accountsGet(accountsGetRequest)).thenReturn(callSuccessful);

        List<PlaidAccountBalances> actual = plaidAccountManager.getBalancesByUserId(userID);
        assertNotNull(actual);
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expectedPlaidAccountBalancesList.get(i).getCurrentBalance(), actual.get(i).getCurrentBalance());
            assertEquals(expectedPlaidAccountBalancesList.get(i).getAvailableBalance(), actual.get(i).getAvailableBalance());
            assertEquals(expectedPlaidAccountBalancesList.get(i).getUserId(), actual.get(i).getUserId());
        }
    }

    @Test
    @DisplayName("Test getPlaidBalancesTreeMap when plaid balances list is null, then throw exception")
    public void testGetPlaidBalances_whenPlaidBalancesListIsNull_thenThrowException()
    {
        assertThrows(NullPointerException.class, () -> {
            plaidAccountManager.getPlaidBalancesTreeMap(null);
        });
    }

    @Test
    @DisplayName("Test getPlaidBalancesTreeMap when plaid balances list is non empty then return TreeMap")
    public void testGetPlaidBalances_whenPlaidBalancesListIsNonEmpty_thenReturnTreeMap() throws IOException, InterruptedException {
        List<PlaidAccountBalances> expectedPlaidAccountBalancesList = new ArrayList<>();
        expectedPlaidAccountBalancesList.add(createTestPlaidAccountBalances());

        TreeMap<Integer, Collection<PlaidAccountBalances>> expectedTreeMap = new TreeMap<>();
        expectedTreeMap.put(1, expectedPlaidAccountBalancesList);

        TreeMap<Integer, Collection<PlaidAccountBalances>> actualTreeMap = plaidAccountManager.getPlaidBalancesTreeMap(expectedPlaidAccountBalancesList);

        assertNotNull(actualTreeMap);
        assertEquals(expectedTreeMap.get(1), actualTreeMap.get(1));
        assertEquals(expectedTreeMap.size(), actualTreeMap.size());
    }

    @Test
    @DisplayName("Test getPlaidBalancesTreeMap when balances list is non empty and plaid account balance is null, then skip plaid account balance and return treeMap")
    public void testGetPlaidBalancesTreeMap_whenBalancesListIsNonEmptyAndPlaidAccountBalancesListIsNull_ThenSkip_thenReturnTreeMap() throws IOException, InterruptedException {
        List<PlaidAccountBalances> expectedPlaidAccountBalancesList = new ArrayList<>();
        expectedPlaidAccountBalancesList.add(null);
        expectedPlaidAccountBalancesList.add(createTestPlaidAccountBalances());
        expectedPlaidAccountBalancesList.add(null);

        TreeMap<Integer, Collection<PlaidAccountBalances>> expectedTreeMap = new TreeMap<>();
        expectedTreeMap.put(1, expectedPlaidAccountBalancesList);

        TreeMap<Integer, Collection<PlaidAccountBalances>> actualTreeMap = plaidAccountManager.getPlaidBalancesTreeMap(expectedPlaidAccountBalancesList);
        assertNotNull(actualTreeMap);
        assertEquals(expectedTreeMap.get(1), actualTreeMap.get(1));
        assertEquals(expectedTreeMap.size(), actualTreeMap.size());
    }

    private PlaidAccountBalances createTestPlaidAccountBalances()
    {
        PlaidAccountBalances plaidAccountBalances = new PlaidAccountBalances();
        plaidAccountBalances.setUserId(1);
        plaidAccountBalances.setAvailableBalance(BigDecimal.valueOf(200));
        plaidAccountBalances.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccountBalances.setAccountId("A1");
        return plaidAccountBalances;
    }


    private AccountBase createTestAccountBase()
    {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAvailable(Double.valueOf(200));
        accountBalance.setCurrent(Double.valueOf(1200));

        AccountBase accountBase = new AccountBase();
        accountBase.setAccountId("A1");
        accountBase.setName("Checking");
        accountBase.setBalances(accountBalance);
        return accountBase;
    }

    private PlaidAccount createTestPlaidAccount() throws IOException {

        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setAccountId("A1");
        plaidAccount.setName("Checking");
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(200.0));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200.0));
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