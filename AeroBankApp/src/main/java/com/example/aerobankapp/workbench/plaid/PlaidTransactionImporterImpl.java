package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.createPlaidTransactionImport;

@Component
public class PlaidTransactionImporterImpl extends AbstractPlaidDataImporter implements PlaidTransactionImporter {
    private final PlaidTransactionConverter plaidTransactionConverter;
    private final PlaidTransactionService plaidTransactionService;
    private final PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;
    private final TransactionCriteriaService transactionCriteriaService;
    private final TransactionStatementService transactionStatementService;
    private final ReferenceNumberGenerator referenceNumberGenerator;
    private final PlaidTransactionManagerImpl plaidTransactionManager;
    private final Logger LOGGER = LoggerFactory.getLogger(PlaidTransactionImporterImpl.class);

    @Autowired
    public PlaidTransactionImporterImpl(PlaidTransactionConverter plaidTransactionConverter,
                                        PlaidTransactionService plaidTransactionService,
                                        PlaidLinkService plaidLinkService,
                                        TransactionCriteriaService transactionCriteriaService,
                                        TransactionStatementService transactionStatementService,
                                        ExternalAccountsService externalAccountsService,
                                        PlaidTransactionManagerImpl plaidTransactionManager) {
        super(externalAccountsService, plaidLinkService);
        this.plaidTransactionConverter = plaidTransactionConverter;
        this.plaidTransactionService = plaidTransactionService;
        this.plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        this.transactionCriteriaService = transactionCriteriaService;
        this.transactionStatementService = transactionStatementService;
        this.referenceNumberGenerator = new ReferenceNumberGeneratorImpl();
        this.externalAccountsService = externalAccountsService;
        this.plaidTransactionManager = plaidTransactionManager;
    }

    public List<PlaidTransaction> getPlaidTransactionsByTransactionType(String transactionType) {
        return null;
    }

    public List<PlaidTransaction> getPendingPlaidTransactionsForUserAndPeriod(LocalDate startDate, LocalDate endDate, int userId) {
        return null;
    }

    public List<PlaidTransaction> getPlaidTransactionsForPeriod(LocalDate startDate, LocalDate endDate, int userId) {
        return null;
    }

    public Boolean checkSinglePlaidTransactionIsLinked(PlaidTransaction plaidTransaction) {
        return null;
    }

    public void createImportedTransactions() {

    }

    public List<TransactionDetail> getTransactionDetailsFromTransactions(List<PlaidTransaction> plaidTransactions) {
        return null;
    }

    private TransactionDetail convertPlaidTransactionToTransactionDetail(PlaidTransaction plaidTransaction) {
        return null;
    }

    public List<TransactionCriteria> getTransactionCriteriaFromPlaidTransactions(List<PlaidTransaction> plaidTransactions) {
        return null;
    }

    private TransactionCriteria convertPlaidTransactionToTransactionCriteria(PlaidTransaction plaidTransaction)
    {
        return null;
    }

    public List<Transfer> getTransfersFromPlaidTransactions(List<PlaidTransaction> plaidTransactionList)
    {
        return null;
    }

    public List<Deposit> getDepositsFromPlaidTransactions(List<PlaidTransaction> plaidTransactions)
    {
        return null;
    }

    public List<Withdraw> getWithdrawalsFromPlaidTransactions(List<PlaidTransaction> plaidTransactions)
    {
        return null;
    }

    public List<TransactionStatement> getTransactionStatementsFromPlaidTransactions(List<PlaidTransaction> plaidTransactions)
    {
        return null;
    }

    public List<LinkedTransactionInfo> prepareLinkedTransactions(UserEntity user, List<PlaidTransaction> plaidTransactionList)
    {
        return null;
    }

    public LinkedTransactionInfo linkTransaction(PlaidTransaction plaidTransaction)
    {
        return null;
    }

    public Map<String, Integer> getTransactionIdToSysAcctIDMap(ExternalAccountsEntity externalAccountsEntity)
    {
        return null;
    }

    @Override
    public List<PlaidTransactionImport> importPlaidTransactions(List<PlaidTransaction> plaidTransactionList) {
        return null;
    }

    @Override
    public PlaidImportResult processPlaidTransaction(PlaidTransaction plaidTransaction) {
        return null;
    }

    public TransactionStatementEntity prepareTransactionStatementEntityFromAmount(AccountEntity accountEntity, PlaidTransactionImport plaidTransactionImport) {
        return null;
    }

    private TransactionStatementEntity createTransactionStatementEntity(AccountEntity accountEntity, String descr, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        return transactionStatementService.createTransactionStatementEntity(accountEntity, descr, debit, credit, balance);
    }

    @Override
    public Boolean saveProcessedPlaidTransactions(List<PlaidTransactionImport> plaidTransactionList) {
        return null;
    }

    private AccountEntity getAccountEntityFromExternalAccountID(String acctID){
        if(acctID == null || acctID.isEmpty())
        {
            throw new IllegalArgumentException("acctID cannot be null or empty");
        }

        Optional<ExternalAccountsEntity> externalAccountsEntity = externalAccountsService.getExternalAccount(acctID);
        AccountEntity accountEntity = null;
        if(externalAccountsEntity.isPresent())
        {
            accountEntity = externalAccountsEntity.get().getAccount();
        }
        return accountEntity;
    }

    private PlaidImportResult createPlaidImportResult(PlaidTransactionImport plaidTransactionImport, boolean isSuccess){
        return new PlaidImportResult(plaidTransactionImport, isSuccess);
    }

    public void createAndSavePlaidTransactionEntity(PlaidTransactionImport plaidTransactionImport) {
        PlaidTransactionEntity plaidTransaction1 = plaidTransactionConverter.convert(plaidTransactionImport);
        plaidTransactionService.save(plaidTransaction1);
    }

    public void createAndSaveTransactionCriteriaEntity(PlaidTransactionImport plaidTransactionImport){
        TransactionCriteriaEntity transactionCriteria = plaidTransactionToTransactionCriteriaConverter.convert(plaidTransactionImport);
        transactionCriteriaService.save(transactionCriteria);
   }

   public void createAndSaveTransactionStatement(PlaidTransactionImport plaidTransactionImport)
   {
       String externalId = plaidTransactionImport.getAcctID();
       AccountEntity accountEntity = getAccountEntityFromExternalAccountID(externalId);

       TransactionStatementEntity transactionStatementEntity = prepareTransactionStatementEntityFromAmount(accountEntity, plaidTransactionImport);
       transactionStatementService.save(transactionStatementEntity);
   }




}
