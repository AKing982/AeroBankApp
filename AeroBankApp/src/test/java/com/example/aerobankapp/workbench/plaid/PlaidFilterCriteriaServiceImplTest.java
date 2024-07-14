package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.plaid.client.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        assertThrows(RuntimeException.class, () -> plaidFilterCriteriaServiceImpl.getPlaidTransactionCriteriaFromResponse(null, null));
    }

    @Test
    @DisplayName("test convert transaction list to criteria list when transaction list is null, then throw exception")
    public void testConvertTransactionListToCriteriaList_whenTransactionListIsNull_thenThrowException() {
        assertThrows(RuntimeException.class, () -> plaidFilterCriteriaServiceImpl.convertTransactionListToCriteriaList(null));
    }

    @Test
    @DisplayName("tes convert transaction list to criteria list when transaction list is not null, then return plaid transaction criteria list")
    public void testConvertTransactionListToCriteriaList_whenTransactionListIsNotNull_thenReturnPlaidTransactionCriteriaList() {

    }

    @Test
    @DisplayName("Test build plaid transaction criteria when transaction is null, then throw exception")
    public void testBuildPlaidTransactionCriteria_whenTransactionIsNull_thenThrowException() {
        assertThrows(RuntimeException.class, () -> plaidFilterCriteriaServiceImpl.buildPlaidTransactionCriteria(null));
    }

    @Test
    @DisplayName("Test build plaid transaction criteria when transaction is valid then return plaid transaction criteria")
    public void testBuildPlaidTransactionCriteria_whenTransactionIsValid_thenReturnPlaidTransactionCriteria() {
        Transaction testTransaction = createTransaction();

        PlaidTransactionCriteria expected = createPlaidTransactionCriteria(testTransaction);
        PlaidTransactionCriteria actual = plaidFilterCriteriaServiceImpl.buildPlaidTransactionCriteria(testTransaction);
        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test getAccountBalancesForTransactions when TransactionsGetResponse has null accounts, then throw exception")
    public void testGetAccountBalancesForTransactions_whenTransactionsGetResponseHasNullAccounts_thenThrowException() {
        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        transactionsGetResponse.setAccounts(null);

        assertThrows(RuntimeException.class, () -> plaidFilterCriteriaServiceImpl.getAccountBalancesForTransactions(transactionsGetResponse));
    }

    @Test
    @DisplayName("Test getAccountBalancesForTransactions when TransactionsGetResponse has empty accounts list, then throw exception")
    public void testGetAccountBalancesForTransactions_whenTransactionsGetResponseHasEmptyAccounts_thenThrowException() {
        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        transactionsGetResponse.setAccounts(new ArrayList<>());

        assertThrows(RuntimeException.class, () -> plaidFilterCriteriaServiceImpl.getAccountBalancesForTransactions(transactionsGetResponse));
    }

    @Test
    @DisplayName("Test getAccountBalancesForTransactions when TransactionsGetResponse has valid accounts list, then return PlaidAccountBalances")
    public void testGetAccountBalancesForTransactions_whenTransactionsGetResponseHasValidAccounts_thenReturnPlaidAccountBalances() {
        List<AccountBase> accountBaseList = new ArrayList<>();
        accountBaseList.add(createAccountBase());

        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        transactionsGetResponse.setAccounts(accountBaseList);

        List<PlaidAccountBalances> expectedAccountBalances = new ArrayList<>();
        expectedAccountBalances.add(createPlaidAccountBalances());

        List<PlaidAccountBalances> actual = plaidFilterCriteriaServiceImpl.getAccountBalancesForTransactions(transactionsGetResponse);
        assertNotNull(actual);
        for(int i = 0; i < actual.size(); i++)
        {
            assertEquals(expectedAccountBalances.get(i).getAccountId(), actual.get(i).getAccountId());
            assertEquals(expectedAccountBalances.get(i).getAvailableBalance(), actual.get(i).getAvailableBalance());
            assertEquals(expectedAccountBalances.get(i).getCurrentBalance(), actual.get(i).getCurrentBalance());
            assertEquals(expectedAccountBalances.get(i).getPendingBalance(), actual.get(i).getPendingBalance());
        }
        assertEquals(expectedAccountBalances.size(), actual.size());
    }

    @Test
    @DisplayName("Test GetAccountBalancesForTransactions when TransactionsGetResponse has valid accounts but account base is null, then skip null account base and return balances")
    public void testGetAccountBalancesForTransactions_whenTransactionsGetResponseHasValidAccounts_AccountBaseItemIsNull_SkipAccountBase_thenReturnPlaidAccountBalances() {
        List<AccountBase> accountBaseList = new ArrayList<>();
        accountBaseList.add(null);
        accountBaseList.add(createAccountBase());

        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        transactionsGetResponse.setAccounts(accountBaseList);

        List<PlaidAccountBalances> expectedAccountBalances = new ArrayList<>();
        expectedAccountBalances.add(createPlaidAccountBalances());

        List<PlaidAccountBalances> actual = plaidFilterCriteriaServiceImpl.getAccountBalancesForTransactions(transactionsGetResponse);
        assertNotNull(actual);
        assertEquals(expectedAccountBalances.size(), actual.size());
    }

    @Test
    @DisplayName("Test GetAccountBalancesForTransactions when TransactionsGetResponse has valid accounts, plaid account balance is null, then throw exception")
    public void testGetAccountBalancesForTransactions_whenTransactionsGetResponseHasValidAccounts_PlaidAccountBalanceIsNull_thenSkipAndReturnBalances() {
        List<AccountBase> accountBaseList = new ArrayList<>();
        accountBaseList.add(createAccountBase());
        accountBaseList.add(createAccountBase());

        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        transactionsGetResponse.setAccounts(accountBaseList);

        List<PlaidAccountBalances> expectedAccountBalances = new ArrayList<>();
        expectedAccountBalances.add(createPlaidAccountBalances());
        expectedAccountBalances.add(null);

        List<PlaidAccountBalances> actual = plaidFilterCriteriaServiceImpl.getAccountBalancesForTransactions(transactionsGetResponse);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    private PlaidAccountBalances createPlaidAccountBalances() {
        PlaidAccountBalances plaidAccountBalances = new PlaidAccountBalances();
        plaidAccountBalances.setAccountId("A1");
        plaidAccountBalances.setCurrentBalance(BigDecimal.valueOf(1200.0));
        plaidAccountBalances.setAvailableBalance(BigDecimal.valueOf(1150.0));
        plaidAccountBalances.setPendingBalance(null);
        return plaidAccountBalances;
    }

    private AccountBase createAccountBase()
    {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAvailable(Double.valueOf(1150.0));
        accountBalance.setCurrent(Double.valueOf(1200.0));

        AccountBase accountBase = new AccountBase();
        accountBase.setName("Test Account");
        accountBase.setAccountId("A1");
        accountBase.setBalances(accountBalance);
        accountBase.setType(AccountType.CREDIT);
        return accountBase;
    }

    private Transaction createTransaction() {
        Transaction testTransaction = new Transaction();
        testTransaction.setAmount(Double.valueOf(120));
        testTransaction.setDate(LocalDate.of(2024, 6, 1));
        testTransaction.setMerchantName("Smiths MarketPlace");
        testTransaction.setOriginalDescription("Groceries #223232");
        return testTransaction;
    }

    private PlaidTransactionCriteria createPlaidTransactionCriteria(Transaction testTransaction) {
        PlaidTransactionCriteria plaidTransactionCriteria = new PlaidTransactionCriteria();
        plaidTransactionCriteria.setAmount(BigDecimal.valueOf(testTransaction.getAmount()));
        plaidTransactionCriteria.setDate(testTransaction.getDate());
        plaidTransactionCriteria.setMerchantName(testTransaction.getMerchantName());
        plaidTransactionCriteria.setDescription(testTransaction.getOriginalDescription());
        return plaidTransactionCriteria;
    }

    @AfterEach
    void tearDown() {
    }
}