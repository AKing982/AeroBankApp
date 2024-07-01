package com.example.aerobankapp.workbench.plaid;

import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.DoNotMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidTokenProcessorImplTest {

    @Mock
    private PlaidApi plaidApi;

    private PlaidTokenProcessorImpl plaidTokenProcessor;

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
    @DisplayName("Test Create Link Token when clientId is valid and LinkTokenRequest is valid, then return LinkResponse")
    public void testCreateLinkToken_whenLinkTokenRequestIsValid_thenReturnLinkResponse() {
        fail();
    }


    @AfterEach
    void tearDown() {
    }
}