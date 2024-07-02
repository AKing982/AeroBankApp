package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.exceptions.InvalidLinkTokenRequestException;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.DoNotMock;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.RetryPolicy;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidTokenProcessorImplTest {

    @Mock
    private PlaidApi plaidApi;

    @InjectMocks
    private PlaidTokenProcessorImpl plaidTokenProcessor;

    private PlaidTokenProcessorImpl spyPlaidTokenProcessor;

    private String clientId = "clientId";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plaidTokenProcessor = new PlaidTokenProcessorImpl(plaidApi);
        spyPlaidTokenProcessor = spy(plaidTokenProcessor);
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

        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken("e234234234234");
        Call<LinkTokenCreateResponse> mockedCall = mock(Call.class);
        when(mockedCall.execute()).thenReturn(Response.success(plaidResponse));
        when(plaidApi.linkTokenCreate(createRequest)).thenReturn(mockedCall);

        LinkTokenCreateResponse response = spyPlaidTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        assertEquals("e234234234234", response.getLinkToken());
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, retry link token attempts, then return response body")
    public void testGetLinkTokenResponseWithRetry_ValidRequest_RetryLinkTokenAttempts_thenReturn_ResponseBody() throws Exception {
        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken("e234234234234");

        Call<LinkTokenCreateResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        Call<LinkTokenCreateResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(plaidResponse));

        when(plaidApi.linkTokenCreate(createRequest)).thenReturn(callUnsuccessful, callSuccessful);
       // when(plaidApi.linkTokenCreate(createRequest)).thenReturn(callSuccessful);

        LinkTokenCreateResponse response = spyTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        assertNotNull(response);
        assertEquals("e234234234234", response.getLinkToken());
        verify(plaidApi, times(2)).linkTokenCreate(createRequest);
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, retry twice for token attempts, and response body null, then throw exception")
    public void testGetLinkTokenResponseWithRetry_ValidRequest_RetryTokenAttempts_ResponseBodyNull_thenThrowException() throws Exception {
        PlaidTokenProcessorImpl spyTokenProcessor = spy(plaidTokenProcessor);
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken(null);

        Call<LinkTokenCreateResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        Call<LinkTokenCreateResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(plaidResponse));

        when(plaidApi.linkTokenCreate(createRequest)).thenReturn(callUnsuccessful, callSuccessful);

        assertThrows(NullPointerException.class, () -> {
            spyTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        });
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, throw exception after max retries, then return exception")
    public void testGetLinkTokenResponseWithRetry_validRequest_throwExceptionAfterMaxRetries_ThenThrowException() throws Exception {
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);

        Call<LinkTokenCreateResponse> callThrowsException = mock(Call.class);
        when(callThrowsException.execute()).thenThrow(new IOException("Mocked Exception"));

        when(plaidApi.linkTokenCreate(createRequest)).thenReturn(callThrowsException);

        assertThrows(IOException.class, () -> {
            spyPlaidTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        });
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, fails on max retries, then exit and throw exception")
    public void testGetLinkTokenResponseWithRetry_ValidRequest_FailsOnMaxRetries_thenThrowException() throws Exception {
        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken("e234234234234");

        Call<LinkTokenCreateResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        when(plaidApi.linkTokenCreate(createRequest))
                .thenReturn(callUnsuccessful, callUnsuccessful);

        assertThrows(InvalidLinkTokenRequestException.class, () -> {
            spyPlaidTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        });
    }

    @Test
    @DisplayName("Test GetLinkTokenResponseWithRetry with valid request, exception is thrown during link token fetch, 2 retries, throw exception")
    public void testGetLinkTokenResponseWithRetry_validRequest_exceptionThrownDuringFetch_TwoRetries_thenThrowException() throws Exception {

        LinkTokenCreateRequest createRequest = buildLinkTokenRequest(clientId);
        LinkTokenCreateResponse plaidResponse = new LinkTokenCreateResponse().linkToken("e234234234234");

        Call<LinkTokenCreateResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenThrow(new IOException());

        when(plaidApi.linkTokenCreate(createRequest))
                .thenReturn(callUnsuccessful, callUnsuccessful);

        assertThrows(IOException.class, () -> {
            spyPlaidTokenProcessor.getLinkTokenResponseWithRetry(createRequest);
        });
    }

    @Test
    @DisplayName("Test exchangeItemPublicToken when Public Token request is null, then throw exception")
    public void testExchangeItemPublicToken_whenPublicTokenRequestIsNull_thenThrowException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> plaidTokenProcessor.exchangeItemPublicToken(null));
    }

    @Test
    @DisplayName("Test exchangeItemPublicToken when Public Token request is valid then return public token exchange response")
    public void testExchangeItemPublicToken_whenPublicTokenIsValid_returnPublicTokenExchangeResponse() throws Exception {
        ItemPublicTokenExchangeRequest itemPublicTokenExchangeRequest = new ItemPublicTokenExchangeRequest();
        ItemPublicTokenExchangeResponse itemPublicTokenExchangeResponse = new ItemPublicTokenExchangeResponse().accessToken("e8242342342");

        ItemPublicTokenExchangeResponse actual = plaidTokenProcessor.exchangeItemPublicToken(itemPublicTokenExchangeRequest);
        assertEquals(itemPublicTokenExchangeResponse.getAccessToken(), actual.getAccessToken());
    }

    private static Call<LinkTokenCreateResponse> UnsuccessfulCall() throws Exception {
        Call<LinkTokenCreateResponse> unsuccessfulCall = mock(Call.class);
        when(unsuccessfulCall.execute())
                .thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal server error")));
        return unsuccessfulCall;
    }

    private static Call<LinkTokenCreateResponse> SuccessfulCall(LinkTokenCreateResponse body) throws Exception {
        Call<LinkTokenCreateResponse> successfulCall = mock(Call.class);
        when(successfulCall.execute())
                .thenReturn(Response.success(body));
        return successfulCall;
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