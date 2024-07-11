package com.example.aerobankapp.controllers;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.services.plaid.PlaidService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.model.TransactionsGetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(value=PlaidController.class, excludeAutoConfiguration= SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
@Import({AppConfig.class, JpaConfig.class})
class PlaidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaidService plaidService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Integer> request = new HashMap<>();

    @MockBean
    private AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;

    @BeforeEach
    public void setUp() {
        request.put("userId", 1);
    }

    @Test
    @DisplayName("Test GetBalances when userId is null then return bad request")
    @WithMockUser
    public void testGetBalances_whenUserIdIsNull_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/plaid/balances")
                .param("userId", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetBalances when userId is valid and AccountsGetResponse accounts is empty, then return status 200")
    public void testGetBalances_whenUserIdIsValidAndAccountsIsEmpty_thenReturnStatus200() throws Exception {

        List<PlaidAccountBalances> plaidAccountBalancesList = Collections.emptyList();

        when(plaidService.getAccountBalances(1)).thenReturn(plaidAccountBalancesList);

        mockMvc.perform(get("/api/plaid/balances")
                .param("userId", "1"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @DisplayName("Test GetBalances when userId and user has balances, return status 200 with balances")
    public void testGetBalances_whenUserIdAndUserHasBalances_thenReturnStatus200() throws Exception {

        List<PlaidAccountBalances> plaidAccountBalancesList = Arrays.asList(createTestPlaidAccountBalances());

        when(plaidService.getAccountBalances(1)).thenReturn(plaidAccountBalancesList);

        mockMvc.perform(get("/api/plaid/balances")
                .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetAccounts when userId is invalid, then return bad request")
    public void testGetAccounts_whenUserIdIsInvalid_thenReturnBadRequest() throws Exception {

        mockMvc.perform(get("/api/plaid/accounts")
                .param("userId", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetAccounts when userId is valid, account base list is null, then return no content")
    public void testGetAccounts_whenUserIdIsValid_AccountBaseListIsNull_thenReturnNoContent() throws Exception {
        when(plaidService.getAccounts(1)).thenReturn(null);
        mockMvc.perform(get("/api/plaid/accounts")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetAccounts when userId is valid, getAccounts is null, then return no content")
    public void testGetAccounts_whenUserIdIsValid_GetAccountsIsNull_thenReturnNoContent() throws Exception {

        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
        accountsGetResponse.setAccounts(null);

        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
        mockMvc.perform(get("/api/plaid/accounts")
                .param("userId", "1"))
                .andExpect(status().isNoContent());
    }


//    @Test
//    @DisplayName("Test Create Link Token when request map is null, then return bad request")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestIsNull_returnBadRequest() throws Exception {
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                        .content("null"))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request contains invalid userId, then return bad request")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsInvalidUserId_returnBadRequest() throws Exception {
//        Map<String, Integer> request = new HashMap<>();
//        request.put("userId", -1);
//
//        String jsonString = objectMapper.writeValueAsString(request);
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonString))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request contains empty key, then return bad request")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsEmptyKey_returnBadRequest() throws Exception {
//        Map<String, Integer> request = new HashMap<>();
//        request.put("", 1);
//
//        String jsonString = objectMapper.writeValueAsString(request);
//
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonString))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request is valid and link token response is valid, then return status 200 ok")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsValidKeyValuePair_returnOk() throws Exception {
//
//        String jsonString = objectMapper.writeValueAsString(request);
//        LinkTokenCreateResponse mockedResponse = new LinkTokenCreateResponse();
//        mockedResponse.setLinkToken("linkToken");
//
//        when(plaidService.createLinkToken("1")).thenReturn(mockedResponse);
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonString))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request is valid and link token is null, then return status 500 error")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsValidKeyValuePair_AndLinkTokenIsNull_returnStatus500() throws Exception {
//       String jsonString = objectMapper.writeValueAsString(request);
//
//       LinkTokenCreateResponse createdResponse = new LinkTokenCreateResponse();
//       createdResponse.setLinkToken(null);
//
//       when(plaidService.createLinkToken("1")).thenReturn(createdResponse);
//
//       mockMvc.perform(post("/api/plaid/create_link_token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonString))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request is valid and link token response is null, then return status 500 error")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsValidKeyValuePair_AndLinkTokenResponseIsNull_returnStatus500() throws Exception
//    {
//        String jsonString = objectMapper.writeValueAsString(request);
//
//        when(plaidService.createLinkToken("1")).thenReturn(null);
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonString))
//                .andExpect(status().isInternalServerError());
//
//    }
//
//    @Test
//    @DisplayName("Test Create Link Token when request is valid and exception is thrown, return status 500 error")
//    @WithMockUser
//    public void testCreateLinkToken_whenRequestContainsValidKeyValuePair_ThrowException_returnStatus500() throws Exception
//    {
//        String jsonString = objectMapper.writeValueAsString(request);
//
//        when(plaidService.createLinkToken("1")).thenThrow(new RuntimeException("Plaid Service Failed."));
//
//        mockMvc.perform(post("/api/plaid/create_link_token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonString))
//                .andExpect(status().isInternalServerError());
//
//    }
//
//    @Test
//    @DisplayName("Test Get Accounts when userId is invalid, then return bad request")
//    @WithMockUser
//    public void testGetAccounts_whenUserIdParamIsInvalid_thenReturnBadRequest() throws Exception
//    {
//        mockMvc.perform(get("/api/plaid/accounts")
//                        .param("userId", "-1"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test Get Accounts when userId is valid, then return status 200")
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_thenReturnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockedResponse = Optional.of(createPlaidAccountsEntity());
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockedResponse);
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                        .param("userId", "1"))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_OptionalIsPresentAndAccessTokenIsNull_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockedResponse = Optional.of(createPlaidAccountsEntityWithNullAccessToken());
//
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockedResponse);
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                        .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_OptionalIsPresentAndAccessTokenIsValid_returnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                        .param("userId", "1"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_OptionalIsPresentAndAccessTokenIsValid_AndAccountsGetResponseIsNull_thenReturnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getAccounts(1)).thenReturn(null);
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_OptionalIsPresentAndAccessTokenIsValid_AndAccountsGetResponseValid_returnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                .param("userId", "1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAccounts_whenUserIdIsValid_ExceptionThrown_ThrowStatus500() throws Exception
//    {
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenThrow(new RuntimeException());
//
//        mockMvc.perform(get("/api/plaid/accounts")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdInvalid_returnBadRequest() throws Exception
//    {
//        int userID = -1;
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", String.valueOf(userID)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_OptionalIsNotPresent_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.empty();
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_OptionalIsPresent_AccessTokenIsNull_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntityWithNullAccessToken());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_OptionalIsPresentAccessTokenIsValid_returnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_OptionalIsPresent_AccessTokenIsValid_AccountGetResponseIsNull_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getAccounts(1)).thenReturn(null);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_OptionalIsPresent_AccountGetResponseValid_returnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_AccountResponseAccountsIsEmpty_returnStatusNotFound() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        AccountsGetResponse accountsGetResponse = new AccountsGetResponse();
//        when(plaidService.getAccounts(1)).thenReturn(accountsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetBalances_whenUserIdValid_exceptionIsThrown_returnStatus500() throws Exception
//    {
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenThrow(new RuntimeException());
//
//        mockMvc.perform(get("/api/plaid/balances")
//                .param("userId", "1"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdInvalid_returnBadRequest() throws Exception {
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "-1")
//                .param("startDate", String.valueOf(LocalDate.of(2024, 6, 1)))
//                .param("endDate", String.valueOf(LocalDate.of(2024, 6, 5))))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdValid_StartDateAndEndDateInvalid_returnBadRequest() throws Exception
//    {
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", "")
//                .param("endDate", ""))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdValid_StartAndEndDateValid_PlaidAccountNotFound_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.empty();
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", String.valueOf(LocalDate.of(2024, 6, 1)))
//                .param("endDate", String.valueOf(LocalDate.of(2024, 6, 5))))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdValid_StartAndEndDateValid_PlaidAccountFound_AccessTokenIsNull_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntityWithNullAccessToken());
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", String.valueOf(LocalDate.of(2024, 6, 1)))
//                .param("endDate", String.valueOf(LocalDate.of(2024, 6, 5))))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdValid_StartAndEndDateValid_PlaidAccountFound_AccessTokenValid_TransactionGetResponseNull_returnStatus500() throws Exception
//    {
//
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        String accessToken = mockOptional.get().getAccessToken();
//        LocalDate startDate = LocalDate.of(2024, 6, 1);
//        LocalDate endDate = LocalDate.of(2024, 6, 5);
//        int userID = 1;
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getTransactions(userID, startDate, endDate)).thenReturn(null);
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", String.valueOf(startDate))
//                .param("endDate", String.valueOf(endDate)))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_whenUserIdValid_StartAndEndDateValid_TransactionGetResponseValid_ReturnStatus200() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        String accessToken = mockOptional.get().getAccessToken();
//        LocalDate startDate = LocalDate.of(2024, 6, 1);
//        LocalDate endDate = LocalDate.of(2024, 6, 5);
//        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
//        int userID = 1;
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenReturn(mockOptional);
//        when(plaidService.getTransactions(userID, startDate, endDate)).thenReturn(transactionsGetResponse);
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", String.valueOf(startDate))
//                .param("endDate", String.valueOf(endDate)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetTransactions_ThrowException_returnStatus500() throws Exception
//    {
//        Optional<PlaidAccountsEntity> mockOptional = Optional.of(createPlaidAccountsEntity());
//        LocalDate startDate = LocalDate.of(2024, 6, 1);
//        LocalDate endDate = LocalDate.of(2024, 6, 5);
//
//        when(plaidService.getPlaidAccountEntityByUserId(1)).thenThrow(new RuntimeException());
//
//        mockMvc.perform(get("/api/plaid/transactions")
//                .param("userId", "1")
//                .param("startDate", String.valueOf(startDate))
//                .param("endDate", String.valueOf(endDate)))
//                .andExpect(status().isInternalServerError());
//    }

    private PlaidAccountsEntity createPlaidAccountsEntity() {
        PlaidAccountsEntity plaidAccountsEntity = new PlaidAccountsEntity();
        plaidAccountsEntity.setCreatedAt(LocalDateTime.now());
        plaidAccountsEntity.setUpdatedAt(LocalDateTime.now());
        plaidAccountsEntity.setId(1L);
        plaidAccountsEntity.setUser(UserEntity.builder().userID(1).build());
        plaidAccountsEntity.setInstitution_name("sandBox");
        plaidAccountsEntity.setAccessToken("e8234234234234");
        plaidAccountsEntity.setItem_id("234324234234234");
        return plaidAccountsEntity;
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



    private PlaidAccountsEntity createPlaidAccountsEntityWithNullAccessToken() {
        PlaidAccountsEntity plaidAccountsEntity = new PlaidAccountsEntity();
        plaidAccountsEntity.setCreatedAt(LocalDateTime.now());
        plaidAccountsEntity.setUpdatedAt(LocalDateTime.now());
        plaidAccountsEntity.setId(1L);
        plaidAccountsEntity.setUser(UserEntity.builder().userID(1).build());
        plaidAccountsEntity.setInstitution_name("sandBox");
        plaidAccountsEntity.setAccessToken(null);
        plaidAccountsEntity.setItem_id("234324234234234");
        return plaidAccountsEntity;
    }







}