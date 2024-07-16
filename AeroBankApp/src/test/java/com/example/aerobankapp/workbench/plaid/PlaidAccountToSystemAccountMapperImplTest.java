package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({JpaConfig.class, AppConfig.class})
class PlaidAccountToSystemAccountMapperImplTest {

    @InjectMocks
    private PlaidAccountToSystemAccountMapperImpl plaidAccountToSystemAccountMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountCodeService accountCodeService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @BeforeEach
    void setUp() {
        plaidAccountToSystemAccountMapper = new PlaidAccountToSystemAccountMapperImpl(accountService, accountCodeService, externalAccountsService);
    }

    @Test
    @DisplayName("Test getUserToAccountIdsMap when plaid account list is null, then throw exception")
    public void testGetUserToAccountIdsMapWhenPlaidAccountListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.getUserToAccountIdsMap(null, null));
    }



    @Test
    @DisplayName("Test getUserToAccountIdsMap when userEntity has invalid userId ")


    @AfterEach
    void tearDown() {
    }
}