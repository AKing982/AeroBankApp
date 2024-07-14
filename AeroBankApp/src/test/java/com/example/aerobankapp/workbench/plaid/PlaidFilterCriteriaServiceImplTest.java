package com.example.aerobankapp.workbench.plaid;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class PlaidFilterCriteriaServiceImplTest {

    @InjectMocks
    private PlaidFilterCriteriaServiceImpl plaidFilterCriteriaServiceImpl;

    @BeforeEach
    void setUp() {
        plaidFilterCriteriaServiceImpl = new PlaidFilterCriteriaServiceImpl();
    }

    @Test
    @DisplayName("Test getPlaidTransactionCriteriaFromResponse when TransactionsGetResponse is null, then throw exception")
    public void testGetPlaidTransactionCriteriaFromResponse_whenTransactionResponseIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> plaidFilterCriteriaServiceImpl.getPlaidTransactionCriteriaFromResponse(null, null));
    }

    @AfterEach
    void tearDown() {
    }
}