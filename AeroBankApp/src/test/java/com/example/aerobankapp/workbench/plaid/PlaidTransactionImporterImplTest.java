package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;
import com.example.aerobankapp.services.*;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;

    @Mock
    private TransactionCriteriaService transactionCriteriaService;

    @Mock
    private TransactionStatementService transactionStatementService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @Mock
    private PlaidLinkService plaidLinkService;

    @BeforeEach
    void setUp() {
        plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        plaidTransactionImporter = new PlaidTransactionImporterImpl(converter, plaidTransactionService, plaidLinkService, transactionCriteriaService, transactionStatementService, externalAccountsService);
    }

    @Test
    @DisplayName("Test importPlaidTransactions when PlaidTransaction list is null, then throw exception")
    public void testImportPlaidTransactions_PlaidTransactionListIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> plaidTransactionImporter.importPlaidTransactions(null));
    }

    @Test
    @DisplayName("Test processPlaidTransaction when plaid transaction is null, then throw exception")
    public void testProcessPlaidTransactions_PlaidTransactionIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> plaidTransactionImporter.processPlaidTransaction(null));
    }

    @Test
    @DisplayName("Test processPlaidTransaction when plaid transaction is valid, then return plaidImportResult")
    public void testProcessPlaidTransaction_whenPlaidTransactionIsValid_thenReturnPlaidImportResult(){
        PlaidTransaction plaidTransaction = createPlaidTransaction();

        PlaidTransactionImport originalImport = createPlaidTransactionImport();

        PlaidImportResult expected = new PlaidImportResult(originalImport, true);
        PlaidTransactionImport expectedTransactionImport = (PlaidTransactionImport) expected.getResult();
        PlaidImportResult actual = plaidTransactionImporter.processPlaidTransaction(plaidTransaction);
        PlaidTransactionImport actualImport = (PlaidTransactionImport) actual.getResult();
        assertEquals(expectedTransactionImport.getTransactionName(), actualImport.getTransactionName());
        assertEquals(expectedTransactionImport.getTransactionId(), actualImport.getTransactionId());
        assertEquals(expectedTransactionImport.getTransactionDate(), actualImport.getTransactionDate());
        assertEquals(expectedTransactionImport.getAmount(), actualImport.getAmount());
        assertEquals(expectedTransactionImport.isPending(), actualImport.isPending());
        assertEquals(expectedTransactionImport.getAcctID(), actualImport.getAcctID());
        assertNotEquals(expectedTransactionImport.getReferenceNumber(), actualImport.getReferenceNumber());
        assertTrue(actual.isSuccessful());
    }



    @Test
    @DisplayName("Test processPlaidTransaction when plaid transaction import is null, then return PlaidImport result with null result and false")
    public void testProcessPlaidTransaction_whenPlaidTransactionImportIsNull_thenReturnPlaidImportResult_withNullResult_AndFalse(){

        PlaidTransaction plaidTransaction = createPlaidTransaction();
        PlaidImportResult expected = new PlaidImportResult(null, false);
        PlaidImportResult actual = plaidTransactionImporter.processPlaidTransaction(plaidTransaction);
        assertEquals(expected, actual);
        assertNotEquals(expected.isSuccessful(), actual.isSuccessful());
    }

    @Test
    @DisplayName("Test prepareTransactionStatementEntityFromAmount when account is null, then throw exception")
    public void testPrepareTransactionStatementEntityFromAmount_whenAccountIsNull_thenThrowException(){
        PlaidTransactionImport transactionImport = createPlaidTransactionImport();
        assertThrows(IllegalArgumentException.class, () ->
                plaidTransactionImporter.prepareTransactionStatementEntityFromAmount(null, transactionImport));
    }

    @Test
    @DisplayName("Test prepareTransactionStatementEntityFromAmount when account and transactionImport are null, then throw exception")
    public void testPrepareTransactionStatementEntityFromAmount_whenAccountAndTransactionImportAreNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionImporter.prepareTransactionStatementEntityFromAmount(null, null);
        });
    }

    @Test
    @DisplayName("Test prepareTransactionStatementEntityFromAmount when account valid")

    @AfterEach
    void tearDown() {
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

    private PlaidTransaction createPlaidTransaction() {
        PlaidTransaction plaidTransaction = new PlaidTransaction();
        plaidTransaction.setTransactionId("e#234234");
        plaidTransaction.setTransactionName("Transaction Test");
        plaidTransaction.setDate(LocalDate.now());
        plaidTransaction.setPending(false);
        plaidTransaction.setAmount(BigDecimal.valueOf(255));
        plaidTransaction.setAccountId("23423423423");
        plaidTransaction.setUserId(1);
        return plaidTransaction;

    }
}