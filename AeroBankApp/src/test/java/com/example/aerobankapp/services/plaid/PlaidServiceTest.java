package com.example.aerobankapp.services.plaid;

import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.request.PlaidApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlaidServiceTest {

    @InjectMocks
    private PlaidService plaidService;

    @MockBean
    private PlaidApi plaidApi;

    @MockBean
    private PlaidAccountsService plaidAccountsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test create Link Token with empty clientUserId string, then throw exception")
    public void testCreateLinkTokenWithEmptyClientUserId_throwException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> plaidService.createLinkToken(""));
    }



    @Test
    @DisplayName("Test create Link Token with valid clientUserId, then return link token response")
    public void testCreateLinkTokenWithValidClientUserId_returnLinkTokenResponse() throws Exception {

    }



    @AfterEach
    void tearDown() {
    }
}