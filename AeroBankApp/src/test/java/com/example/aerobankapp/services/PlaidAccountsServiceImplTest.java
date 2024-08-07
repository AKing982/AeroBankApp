package com.example.aerobankapp.services;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({JpaConfig.class, AppConfig.class})
class PlaidAccountsServiceImplTest {

    @MockBean
    private PlaidLinkServiceImpl plaidAccountsService;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test Save Plaid Accounts Entity when Plaid Account is null, then throw exceptoin")
    public void testSavePlaidAccount_whenPlaidAccountIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountsService.save(null);
        });
    }

    @Test
    @DisplayName("Test Save Plaid AccountsEntity when plaid account criteria is null, then throw exception")
    public void testSavePlaidAccount_whenPlaidAccountCriteriaIsNull_thenThrowException() {
        PlaidLinkEntity plaidAccountsEntity = new PlaidLinkEntity();
        plaidAccountsEntity.setId(1L);
        plaidAccountsEntity.setUser(null);
        plaidAccountsEntity.setAccessToken(null);
        plaidAccountsEntity.setItem_id(null);
        plaidAccountsEntity.setInstitution_name(null);
        plaidAccountsEntity.setCreatedAt(null);
        plaidAccountsEntity.setUpdatedAt(null);

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountsService.save(plaidAccountsEntity);
        });
    }

    @Test
    @DisplayName("Test Save Plaid AccountsEntity when plaid account is valid")
    public void testSavePlaidAccount_whenPlaidAccountIsValid_thenThrowException() {
        PlaidLinkEntity plaidAccountsEntity = new PlaidLinkEntity();
        plaidAccountsEntity.setId(1L);
        plaidAccountsEntity.setUser(UserEntity.builder().userID(1).build());
        plaidAccountsEntity.setAccessToken("accessToken");
        plaidAccountsEntity.setItem_id("itemId");
        plaidAccountsEntity.setInstitution_name("institutionName");
        plaidAccountsEntity.setCreatedAt(LocalDateTime.now());
        plaidAccountsEntity.setUpdatedAt(LocalDateTime.now());

        plaidAccountsService.save(plaidAccountsEntity);

        verify(plaidAccountsService).save(plaidAccountsEntity);
    }

    @Test
    @DisplayName("Test hasPlaidAccount when userID is invalid, then throw exception")
    public void testHasPlaidAccount_whenUserIDIsInvalid_thenThrowException() {
        int userID = -1;

        when(plaidAccountsService.hasPlaidLink(userID)).thenThrow(new IllegalArgumentException("Invalid UserID"));

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountsService.hasPlaidLink(userID);
        });
    }

    @Test
    @DisplayName("Test hasPlaidAccount when userID is valid, then return true")
    public void testHasPlaidAccount_whenUserIDIsValid_thenReturnTrue() {
        int userID = 1;

        when(plaidAccountsService.hasPlaidLink(userID)).thenReturn(true);

        boolean result = plaidAccountsService.hasPlaidLink(userID);
        assertTrue(result);
    }





    @AfterEach
    void tearDown() {
    }
}