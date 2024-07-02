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
   @DisplayName("Test exchangePublicToken when public token is empty or null")
   public void testExchangePublicToken_whenPublicTokenIsNull_thenThrowException() throws Exception {
        assertThrows(NullPointerException.class, () -> plaidTokenProcessor.exchangePublicToken(null));
        assertThrows(IllegalArgumentException.class, () -> plaidTokenProcessor.exchangePublicToken(""));
   }

   @Test
   @DisplayName("Test exchangePublicToken when public token is valid, then return PublicToken response")
   public void testExchangePublicToken_whenPublicTokenIsValid_thenReturnPublicTokenResponse() throws Exception {
        String publicToken = "e234234234";
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
        Call<ItemPublicTokenExchangeResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(new ItemPublicTokenExchangeResponse().accessToken(publicToken)));

        when(plaidApi.itemPublicTokenExchange(request)).thenReturn(callSuccessful);

        ItemPublicTokenExchangeResponse response = plaidTokenProcessor.exchangePublicToken(publicToken);
        assertEquals(publicToken, response.getAccessToken());
    }

    @Test
    @DisplayName("Test exchangePublicToken when public token is valid, exchange request is null, throw exception")
    public void testExchangePublicToken_whenPublicTokenIsValid_ExchangeRequestIsNull_thenThrowException() throws Exception {
        String publicToken = "e234234234";
        Call<ItemPublicTokenExchangeResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(new ItemPublicTokenExchangeResponse().accessToken(publicToken)));

        when(plaidApi.itemPublicTokenExchange(null)).thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class, () -> {
            plaidTokenProcessor.exchangePublicToken(publicToken);
        });
    }

    @Test
    @DisplayName("Test exchangePublicTokenResponseWithRetry when request is null, then throw exception")
    public void testExchangePublicTokenResponseWithRetry_whenRequestIsNull_thenThrowException() throws Exception {
        assertThrows(NullPointerException.class, () -> {plaidTokenProcessor.exchangePublicToken(null);
        });
    }

    @Test
    @DisplayName("Test exchangePublicTokenResponseWithRetry when request is valid, then return ItemPublicTokenResponse")
    public void testExchangePublicTokenResponse_whenRequestIsValid_thenReturnItemPublicTokenResponse() throws Exception {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken("e234234234234");
        Call<ItemPublicTokenExchangeResponse> callSuccessful = mock(Call.class);

        when(callSuccessful.execute()).thenReturn(Response.success(new ItemPublicTokenExchangeResponse().accessToken("e234234234234")));
        when(plaidApi.itemPublicTokenExchange(request)).thenReturn(callSuccessful);

        ItemPublicTokenExchangeResponse response = plaidTokenProcessor.exchangePublicTokenResponseWithRetry(request);
        assertEquals("e234234234234", response.getAccessToken());
    }

    //TODO: Rewrite Test
    @Test
    @DisplayName("Test exchangePublicTokenResponseWithRetry when request is valid and 1 re-attempt, then return response")
    public void testExchangePublicTokenResponseWithRetry_whenRequestIsValid_AndOneReAttempts_ThenReturnResponse() throws Exception {
        String publicToken = "e234234234234";
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
        Call<ItemPublicTokenExchangeResponse> callSuccessful = mock(Call.class);
        when(callSuccessful.execute()).thenReturn(Response.success(new ItemPublicTokenExchangeResponse().accessToken(publicToken)));

        Call<ItemPublicTokenExchangeResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        when(plaidApi.itemPublicTokenExchange(request)).thenReturn(callSuccessful);

        ItemPublicTokenExchangeResponse response = plaidTokenProcessor.exchangePublicTokenResponseWithRetry(request);
        assertEquals("e234234234234", response.getAccessToken());
    }

    @Test
    @DisplayName("Test exchangePublicTokenResponseWithRetry when request is valid and throw exception after 2 re-attempts, then throw exception")
    public void testExchangePublicTokenResponseWithRetry_whenRequestIsValid_ThrowExceptionAfterTwoRetries_ThenThrowException() throws Exception
    {
        String publicToken = "e234234234234";
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest().publicToken(publicToken);
        ItemPublicTokenExchangeResponse response = new ItemPublicTokenExchangeResponse().accessToken(publicToken);
        Call<ItemPublicTokenExchangeResponse> callUnsuccessful = mock(Call.class);
        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        when(callUnsuccessful.execute()).thenReturn(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "Internal Server Error")));

        when(plaidApi.itemPublicTokenExchange(request)).thenReturn(callUnsuccessful);

        assertThrows(IllegalArgumentException.class, () -> {
            plaidTokenProcessor.exchangePublicTokenResponseWithRetry(request);
        });
    }

    @Test
    @DisplayName("Test exchangePublicTokenResponseWithRetry when request is valid and throw exception after three re-attempts")
    public void testExchangePublicTokenResponseWithRetry_whenRequestIsValid_throwExceptionAfterThreeReAttempts() throws Exception
    {

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