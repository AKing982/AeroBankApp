package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.converter.TransactionToPlaidTransactionConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.ExternalTransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.PlaidTransactionsResponseException;
import com.example.aerobankapp.model.LinkedTransactionInfo;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;
import com.example.aerobankapp.services.*;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetResponse;
import org.checkerframework.checker.signedness.qual.SignedPositive;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class PlaidTransactionImporterImplTest {

    @InjectMocks
    private PlaidTransactionImporterImpl plaidTransactionImporter;

    @Mock
    private PlaidTransactionConverter converter;

    @Mock
    private PlaidUtil plaidUtil;

    @Mock
    private PlaidTransactionService plaidTransactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;

    @Mock
    private TransactionCriteriaService transactionCriteriaService;

    @Mock
    private TransactionStatementService transactionStatementService;

    @Mock
    private PlaidTransactionManagerImpl plaidTransactionManager;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @Mock
    private ExternalTransactionService externalTransactionService;

    @Mock
    private PlaidLinkService plaidLinkService;

    private TransactionToPlaidTransactionConverter transactionToPlaidTransactionConverter;

    @BeforeEach
    void setUp() {

        transactionToPlaidTransactionConverter = new TransactionToPlaidTransactionConverter();
        plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        plaidTransactionImporter = new PlaidTransactionImporterImpl(converter, plaidTransactionService, externalTransactionService,accountService, plaidLinkService, transactionCriteriaService, transactionStatementService,  externalAccountsService, plaidTransactionManager);
    }

    @Test
    @DisplayName("Test getPlaidTransactionsForPeriod when startDate and endDate is null, then throw exception")
    public void testGetPlaidTransactionsForPeriod_whenStartDateAndEndDateIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionImporter.getPlaidTransactionsForPeriod(null, null, 1);
        });
    }

    @Test
    @DisplayName("Test getPlaidTransactionsForPeriod when startDate and EndDate and UserId Valid, then return plaid transaction list")
    public void testGetPlaidTransactionsForPeriod_whenStartDateAndEndDateAndUserIdValid_thenReturnTransactionlist() throws IOException {
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        int userId = 1;
        List<PlaidTransaction> plaidTransactions = Arrays.asList(createPlaidTransaction());

        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        List<Transaction> transactions = transactionsGetResponse.getTransactions();
        transactions.add(createTransaction());
        when(plaidTransactionManager.getTransactionResponse(userId, startDate,endDate)).thenReturn(transactionsGetResponse);

        List<PlaidTransaction> actual = plaidTransactionImporter.getPlaidTransactionsForPeriod(startDate, endDate, userId);
        assertEquals(plaidTransactions.size(), actual.size());
    }

    @Test
    @DisplayName("Test getPlaidTransactionsForPeriod when Transaction is null, then skip and return transaction list")
    public void testGetPlaidTransactionsForPeriod_whenTransactionIsNull_thenSkipAndReturnTransactionList() throws IOException {
        // Assert
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        int userId = 1;
        List<PlaidTransaction> plaidTransactions = Arrays.asList(createPlaidTransaction(), createPlaidTransaction());
        TransactionsGetResponse transactionsGetResponse = new TransactionsGetResponse();
        List<Transaction> transactions = transactionsGetResponse.getTransactions();
        transactions.add(createTransaction());
        transactions.add(null);
        transactions.add(createTransaction());

        when(plaidTransactionManager.getTransactionResponse(userId, startDate, endDate)).thenReturn(transactionsGetResponse);

        List<PlaidTransaction> actual = plaidTransactionImporter.getPlaidTransactionsForPeriod(startDate, endDate, userId);
        assertNotNull(actual);
        assertEquals(plaidTransactions.size(), actual.size());
        for(int i = 0; i < actual.size(); i++)
        {
            assertEquals(plaidTransactions.get(i).getTransactionId(), actual.get(i).getTransactionId());
            assertEquals(plaidTransactions.get(i).getTransactionName(), actual.get(i).getTransactionName());
            assertEquals(plaidTransactions.get(i).isPending(), actual.get(i).isPending());
            assertEquals(plaidTransactions.get(i).getAmount(), actual.get(i).getAmount());
            assertEquals(plaidTransactions.get(i).getAccountId(), actual.get(i).getAccountId());
        }
    }

    @Test
    @DisplayName("Test getPlaidTransactionsForPeriod when transaction response is null, then throw exception")
    public void testGetPlaidTransactionsForPeriod_whenTransactionResponseIsNull_thenThrowException() throws IOException {
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        int userId = 1;
        when(plaidTransactionManager.getTransactionResponse(userId, startDate, endDate)).thenThrow(new PlaidTransactionsResponseException(""));

        assertThrows(PlaidTransactionsResponseException.class, () -> {
            plaidTransactionImporter.getPlaidTransactionsForPeriod(startDate, endDate, userId);
        });
    }

    @Test
    @DisplayName("Test checkSinglePlaidTransactionIsLinked when plaid transaction is null, then return false")
    public void testCheckSinglePlaidTransactionIsLinked_whenPlaidTransactionIsNull_thenReturnFalse() throws IOException {
        assertFalse(plaidTransactionImporter.checkSinglePlaidTransactionIsLinked(null));
    }

    @Test
    @DisplayName("Test checkSinglePlaidTransactionIsLinked when plaid transaction valid, then return true")
    public void testCheckSinglePlaidTransactionIsLinked_whenPlaidTransactionValid_thenReturnTrue() throws IOException {
        PlaidTransaction plaidTransaction = createPlaidTransaction();

        when(externalTransactionService.findByTransactionId(plaidTransaction.getTransactionId())).thenReturn(Optional.of(createExternalTransactionEntity()));

        Boolean result = plaidTransactionImporter.checkSinglePlaidTransactionIsLinked(plaidTransaction);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test checkSinglePlaidTransactionIsLinked when plaid acctID linked to no sysAcctID, then return false")
    public void testCheckSinglePlaidTransactionisLinked_whenPlaidAcctIDNotLinkedToSysAcctID_thenReturnFalse() throws IOException {
        PlaidTransaction plaidTransaction = createPlaidTransaction();
        ExternalAccountsEntity externalAccounts = new ExternalAccountsEntity();
        externalAccounts.setAccount(null);
        externalAccounts.setExternalAcctID("e232323233");

        ExternalTransactionEntity externalTransactionEntity = new ExternalTransactionEntity();
        externalTransactionEntity.setExternalAccounts(externalAccounts);
        externalTransactionEntity.setExternalTransactionId("e123456789");

        when(externalTransactionService.findByTransactionId(plaidTransaction.getTransactionId())).thenReturn(Optional.of(externalTransactionEntity));
        Boolean result = plaidTransactionImporter.checkSinglePlaidTransactionIsLinked(plaidTransaction);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test prepareLinkedTransactions when user entity or plaid transaction list is null, then throw exception")
    public void testPrepareLinkedTransactions_whenUserEntityOrPlaidTransactionListIsNull_thenThrowException() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionImporter.prepareLinkedTransactions(null, null);
        });
    }

    @Test
    @DisplayName("Test prepareLinkedTransactions when user entity valid and plaidTransactionlist is empty, then return empty list")
    public void testPrepareLinkedTransactions_whenUserEntityValidAndPlaidTransactionListIsEmpty_thenReturnEmptyList() throws IOException {
        UserEntity user = createUserEntity();
        List<PlaidTransaction> plaidTransactions = new ArrayList<>();

        List<LinkedTransactionInfo> linkedTransactionInfos = new ArrayList<>();
        List<LinkedTransactionInfo> actual = plaidTransactionImporter.prepareLinkedTransactions(user, plaidTransactions);
        assertNotNull(actual);
        assertEquals(plaidTransactions.size(), actual.size());
    }

    @Test
    @DisplayName("Test prepareLinkedTransactions when user entity valid and plaid transaction list valie, then return LinkedTransactionInfo")
    public void testPrepareLinkedTransactions_whenUserEntityValidAndPlaidTransactionListValid_thenReturnLinkedTransactionInfo() throws IOException {
        UserEntity user = createUserEntity();
        PlaidTransaction plaidTransaction = createPlaidTransaction();
        PlaidTransaction plaidTransaction1 = createPlaidTransaction();
        List<PlaidTransaction> plaidTransactions = new ArrayList<>();
        plaidTransactions.add(plaidTransaction);
        plaidTransactions.add(plaidTransaction1);

        List<LinkedTransactionInfo> expected = new ArrayList<>();
        LinkedTransactionInfo linkedTransactionInfo1 = new LinkedTransactionInfo(plaidTransaction.getTransactionId(), 1);
        LinkedTransactionInfo linkedTransactionInfo2 = new LinkedTransactionInfo(plaidTransaction.getTransactionId(), 2);
        expected.add(linkedTransactionInfo1);
        expected.add(linkedTransactionInfo2);

        AccountEntity account1 = createAccountEntity("depository", "01", 1, user, "checking");
        AccountEntity account2 = createAccountEntity("depository", "02", 2, user, "savings");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        when(accountService.getListOfAccountsByUserID(user.getUserID())).thenReturn(accounts);

        List<LinkedTransactionInfo> actual = plaidTransactionImporter.prepareLinkedTransactions(user, plaidTransactions);
        assertNotNull(actual);
        assertEquals(plaidTransactions.size(), actual.size());
        for(int  i = 0; i < actual.size(); i++)
        {
            assertEquals(expected.get(i).getTransactionId(), actual.get(i).getTransactionId());
            assertEquals(expected.get(i).getSysAcctID(), actual.get(i).getSysAcctID());
        }
    }

    @Test
    @DisplayName("Test linkTransaction when plaidTransaction is null or AccountEntity is null, then throw exception")
    public void testLinkTransaction_whenPlaidTransactionIsNullOrAccountEntityIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionImporter.linkTransaction(null, null);
        });
    }

    @Test
    @DisplayName("Test linkTransaction when plaid transaction and account are valid, then return linkedTransactionInfo")
    public void testLinkTransaction_whenPlaidTransactionAndAccountAreValid_thenReturnLinkedTransactionInfo(){
        PlaidTransaction plaidTransaction = createPlaidTransaction();
        AccountEntity accountEntity = createAccountEntity("depository", "01", 1, createUserEntity(), "checking");

        LinkedTransactionInfo expected = new LinkedTransactionInfo("e2323232", 1);
        LinkedTransactionInfo actual = plaidTransactionImporter.linkTransaction(plaidTransaction, accountEntity);

    }



    @AfterEach
    void tearDown() {
    }

    private AccountEntity createAccountEntity(String type, String acctType, int acctID, UserEntity user, String subType)
    {
        return AccountEntity.builder()
                .accountType(acctType)
                .subtype(subType)
                .type(type)
                .acctID(acctID)
                .user(user)
                .build();
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(1);
        return userEntity;
    }

    private PlaidTransactionImport createPlaidTransactionImport(){
        PlaidTransactionImport plaidTransactionImport = new PlaidTransactionImport();
        plaidTransactionImport.setTransactionDate(LocalDate.now());
        plaidTransactionImport.setTransactionId("e#234234");
        plaidTransactionImport.setTransactionName("Transaction Test");
        plaidTransactionImport.setPosted(LocalDate.now());
        plaidTransactionImport.setAmount(BigDecimal.valueOf(255));
        plaidTransactionImport.setPending(false);
        plaidTransactionImport.setAcctID("23423423423");
        return plaidTransactionImport;
    }

    private ExternalAccountsEntity createExternalAccountsEntity(){
        ExternalAccountsEntity externalAccountsEntity = new ExternalAccountsEntity();
        externalAccountsEntity.setExternalAcctID("e232323233");
        externalAccountsEntity.setAccount(AccountEntity.builder().acctID(1).build());
        return externalAccountsEntity;
    }

    private ExternalTransactionEntity createExternalTransactionEntity(){
        ExternalTransactionEntity externalTransactionEntity = new ExternalTransactionEntity();
        externalTransactionEntity.setExternalAccounts(createExternalAccountsEntity());
        externalTransactionEntity.setExternalTransactionId("e123456789");
        return externalTransactionEntity;
    }

    private Transaction createTransaction()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(Double.valueOf(120.0));
        transaction.setTransactionId("e#123456");
        transaction.setAccountId("23423423423");
        transaction.setDate(LocalDate.of(2024, 6, 1));
        transaction.setPending(false);
        transaction.setMerchantName("Merchant Test");
        transaction.setName("Transaction Test");
        return transaction;
    }

    private PlaidTransaction createPlaidTransaction() {
        PlaidTransaction plaidTransaction = new PlaidTransaction();
        plaidTransaction.setTransactionId("e#123456");
        plaidTransaction.setTransactionName("Transaction Test");
        plaidTransaction.setDate(LocalDate.of(2024, 6, 1));
        plaidTransaction.setPending(false);
        plaidTransaction.setAmount(BigDecimal.valueOf(120.0));
        plaidTransaction.setAccountId("e232323233");
        plaidTransaction.setUserId(1);
        return plaidTransaction;

    }

}