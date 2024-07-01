package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.exceptions.InvalidLinkTokenRequestException;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.DoNotMock;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidTokenProcessorImplTest {

    @Mock
    private PlaidApi plaidApi;

    @InjectMocks
    private PlaidTokenProcessorImpl plaidTokenProcessor;

    private String clientId = "clientId";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plaidTokenProcessor = new PlaidTokenProcessorImpl(plaidApi);
    }

    @Test
    @DisplayName("Test Create Link Token when clientUserId is empty or null, then throw exception")
    public void testCreateLinkToken_whenClientUserIdIsEmpty_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> plaidTokenProcessor.createLinkToken(null));
    }

    @Test
    @DisplayName("Test Create Link Token when clientId is valid and LinkTokenRequest is null, then throw exception")
    public void testCreateLinkToken_whenLinkTokenRequestIsNull_thenThrowException() {
        PlaidTokenProcessorImpl spyClass = spy(plaidTokenProcessor);
        doReturn(null).when(spyClass).buildLinkTokenRequest("1");

        assertThrows(IllegalArgumentException.class, () -> spyClass.createLinkToken("1"));
    }

    @Test
    @DisplayName("Test Create Link Token when clientId is valid and LinkTokenRequest is valid and LinkTokenResponse is null, then throw NullPointerException")
    public void testCreateLinkToken_whenLinkTokenRequestIsValid_LinkTokenResponseIsNull_thenThrowException() throws Exception {

        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);

        doReturn(createRequest).when(spyTokenProcessor).buildLinkTokenRequest(clientId);
        doReturn(Response.success(null)).when(spyTokenProcessor).getLinkTokenCreateResponse(createRequest);

        assertThrows(NullPointerException.class, () ->
                plaidTokenProcessor.createLinkToken(clientId));
    }

    @Test
    @DisplayName("Test Create Link Token when clientId is valid and link token request is valid and link token response is valid, then return response")
    public void testCreateLinkToken_whenLinkTokenRequestIsValid_linkTokenResponseIsValid_thenReturnResponse() throws Exception {
        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);

        doReturn(createRequest).when(spyTokenProcessor).buildLinkTokenRequest(clientId);

        doReturn(Response.success("success")).when(spyTokenProcessor).getLinkTokenCreateResponse(createRequest);

        LinkTokenCreateResponse response = plaidTokenProcessor.createLinkToken(clientId);
        assertEquals("success", response.getLinkToken());
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with null request, then throw exception")
    public void testGetLinkTokenResponseWithRetry_whenRequestIsNull_thenThrowException() {
        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);

        doReturn(null).when(spyTokenProcessor).buildLinkTokenRequest(clientId);
        assertThrows(InvalidLinkTokenRequestException.class, () -> spyTokenProcessor.getLinkTokenResponseWithRetry(null));
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, then return response body")
    public void testGetLinkTokenResponseWithRetry_whenRequestIsValid_thenReturnResponse() throws Exception {
        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);

        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken("e234234234234");
        doReturn(Response.success(plaidResponse)).when(plaidApi).linkTokenCreate(any());

        LinkTokenCreateResponse response = spyTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        assertEquals("e234234234234", response.getLinkToken());
    }

    public LinkTokenCreateRequest buildLinkTokenRequest(String clientUserId)
    {
        return new LinkTokenCreateRequest()
                .user(new LinkTokenCreateRequestUser().clientUserId(clientUserId))
                .clientName("Utah Kings Credit Union")
                .products(Arrays.asList(Products.AUTH, Products.TRANSACTIONS, Products.STATEMENTS, Products.RECURRING_TRANSACTIONS))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en");
    }


    @AfterEach
    void tearDown() {
    }
}