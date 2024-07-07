package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.PlaidAccessTokenNotFoundException;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
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
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidTransactionManagerImplTest {

    @InjectMocks
    private PlaidTransactionManagerImpl plaidTransactionManager;

    @Mock
    private PlaidApi plaidApi;

    private PlaidTransactionManagerImpl spyPlaidTransactionManager;

    @Mock
    private PlaidAccountsService plaidAccountsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plaidTransactionManager = new PlaidTransactionManagerImpl(plaidAccountsService, plaidApi);
        spyPlaidTransactionManager = spy(plaidTransactionManager);
    }

    @Test
    @DisplayName("Test getTransactionResponseWithRetry when request is null, then throw exception")
    public void testGetTransactionResponseWithRetry_whenRequestIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                plaidTransactionManager.getTransactionResponseWithRetry(null));
    }

    @Test
    @DisplayName("Test getTransactionResponseWithRetry when request is valid ")
    public void testGetTransactionResponseWithRetry_whenRequestIsValid_thenReturnResponse() throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest().accessToken("e23232323")
                .startDate(LocalDate.of(2024, 6, 5))
                .endDate(LocalDate.of(2024, 6, 6));

        TransactionsGetResponse response = new TransactionsGetResponse();
        Call<TransactionsGetResponse> callSuccessful = mock(Call.class);

        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.transactionsGet(any())).thenReturn(callSuccessful);
        TransactionsGetResponse result = plaidTransactionManager.getTransactionResponseWithRetry(request);
        assertEquals(response, result);
    }

    @Test
    @DisplayName("Test GetTransactionResponseWithRetry when request is valid, and 2 retries to fetch, then return response")
    public void testGetTransactionResponseWithRetry_whenRequestValid_AndTwoRetries_thenReturnResponse() throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest().accessToken("e23232323")
                .startDate(LocalDate.of(2024, 6, 5))
                .endDate(LocalDate.of(2024, 6, 6));

        TransactionsGetResponse response = new TransactionsGetResponse();
        Call<TransactionsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        Call<TransactionsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.transactionsGet(any())).thenReturn(callUnsuccessful, callSuccessful);
        TransactionsGetResponse result = plaidTransactionManager.getTransactionResponseWithRetry(request);
        assertEquals(response, result);
    }

    @Test
    @DisplayName("Test GetTransactionResponseWithRetry when max attempts, then break out of loop and throw exception")
    public void testGetTransactionResponseWithRetry_whenMaxAttempts_thenThrowException() throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest().accessToken("e23232323")
                .startDate(LocalDate.of(2024, 6, 5))
                .endDate(LocalDate.of(2024, 6, 6));

        Call<TransactionsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        when(plaidApi.transactionsGet(any())).thenReturn(callUnsuccessful, callUnsuccessful, callUnsuccessful, callUnsuccessful, callUnsuccessful);

        assertThrows(IOException.class, () -> plaidTransactionManager.getTransactionResponseWithRetry(request));
    }

    @Test
    @DisplayName("Test GetTransactionResponseWithRetry when response successful, and response body is null, then re-attempt and return response")
    public void testGetTransactionResponseWithRetry_whenResponseSuccessful_andResponseBodyIsNull_thenReattemptAndReturnResponse() throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest().accessToken("e23232323")
                .startDate(LocalDate.of(2024, 6, 5))
                .endDate(LocalDate.of(2024, 6, 6));

        Call<TransactionsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(null));

        TransactionsGetResponse response = new TransactionsGetResponse();
        Call<TransactionsGetResponse> callSuccessful2 = mock(Call.class);
        when(callSuccessful2.execute()).thenReturn(Response.success(response));

        when(plaidApi.transactionsGet(any())).thenReturn(callSuccessful, callSuccessful2);

        TransactionsGetResponse result = plaidTransactionManager.getTransactionResponseWithRetry(request);
        assertEquals(response, result);
    }

    @Test
    @DisplayName("Test GetTransactionResponseWithRetry when exception is thrown during response fetch")
    public void testGetTransactionResponseWithRetry_whenExceptionIsThrown_thenThrowException() throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest().accessToken("e23232323")
                .startDate(LocalDate.of(2024, 6, 5))
                .endDate(LocalDate.of(2024, 6, 6));

        Call<TransactionsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenThrow(new IOException());

        assertThrows(IOException.class, () -> plaidTransactionManager.getTransactionResponseWithRetry(request));
    }

    @Test
    @DisplayName("Test getTransactionResponse when userID, StartDate and EndDate are null, then throw Exception")
    public void testGetTransactionResponseWithRetry_whenUserIDAndStartAndEndDateAreNull_thenThrowException() throws IOException {
        assertThrows(IllegalArgumentException.class, () ->
                plaidTransactionManager.getTransactionResponse(-1, null, null));
    }

    @Test
    @DisplayName("Test getTransactionResponse when userId is valid and user doesn't have accessToken, then throw exception")
    public void testGetTransactionRequest_whenUserIdIsValidAndUserDoesNotHaveAccessToken_thenThrowException() throws IOException {
        int userID = 1;
        LocalDate startDate = LocalDate.of(2024, 6, 5);
        LocalDate endDate = LocalDate.of(2024, 6, 6);

        TransactionsGetRequest request = new TransactionsGetRequest().accessToken(null);
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(Optional.empty());

        assertThrows(PlaidAccessTokenNotFoundException.class, () -> {
            plaidTransactionManager.getTransactionResponse(userID, startDate, endDate);
        });
    }

    @Test
    @DisplayName("Test getTransactionResponse when userId is valid and access token exists, then return response")
    public void testGetTransactionResponse_whenUserIdIsValidAndAccessTokenExists_thenReturnResponse() throws IOException {
        int userID = 1;
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 6);
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(Optional.of(createPlaidAccountEntity()));

        TransactionsGetResponse response = new TransactionsGetResponse();
        Call<TransactionsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.transactionsGet(any())).thenReturn(callSuccessful);
        TransactionsGetResponse result = plaidTransactionManager.getTransactionResponse(userID, startDate, endDate);
        assertEquals(response, result);
    }

    @Test
    @DisplayName("Test GetTransactionResponse when parameters valid and access Token is empty/null then throw Exception")
    public void testGetTransactionResponse_whenParametersValidAndAccessTokenIsNull_thenThrowException() throws IOException {
        int userID = 1;
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 6);
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(Optional.of(createPlaidAccountEntityWithoutAccessToken()));
        assertThrows(PlaidAccessTokenNotFoundException.class, () -> {
            plaidTransactionManager.getTransactionResponse(userID, startDate, endDate);
        });
    }

    @Test
    @DisplayName("Test GetTransactionResponse when parameters valid, and initial fetch fails, call TransactionResponseWithRetry")
    public void testGetTransactionResponse_whenParametersValidAndInitialFetchFails_thenCallTransactionResponseWithRetryForResponse() throws IOException {
        int userID = 1;
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 6);
        when(plaidAccountsService.findPlaidAccountEntityByUserId(userID)).thenReturn(Optional.of(createPlaidAccountEntity()));

        Call<TransactionsGetResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.success(null));

        TransactionsGetResponse response = new TransactionsGetResponse();
        Call<TransactionsGetResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(response));

        when(plaidApi.transactionsGet(any())).thenReturn(callUnsuccessful, callSuccessful);
        TransactionsGetResponse result = plaidTransactionManager.getTransactionResponse(userID, startDate, endDate);
        assertEquals(response, result);
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

    @Test
    public void testGetTransactionResponse_whenAccessTokenValid_StartAndEndDateValid_thenReturnTransactionResponse()
    {

    }

    @AfterEach
    void tearDown() {
    }
}