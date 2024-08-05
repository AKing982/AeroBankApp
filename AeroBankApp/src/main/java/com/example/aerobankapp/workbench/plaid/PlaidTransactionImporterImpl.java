package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.converter.TransactionToPlaidTransactionConverter;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.PlaidTransactionsResponseException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.createPlaidTransactionImport;

@Component
public class PlaidTransactionImporterImpl extends AbstractPlaidDataImporter implements PlaidTransactionImporter {
    private final PlaidTransactionConverter plaidTransactionConverter;
    private final TransactionToPlaidTransactionConverter transactionToPlaidTransactionConverter;
    private final PlaidTransactionService plaidTransactionService;
    private final ExternalTransactionService externalTransactionService;
    private final PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;
    private final TransactionCriteriaService transactionCriteriaService;
    private final TransactionStatementService transactionStatementService;
    private final ReferenceNumberGenerator referenceNumberGenerator;
    private final PlaidTransactionManagerImpl plaidTransactionManager;

    private List<LinkedTransactionInfo> linkedTransactionsList = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(PlaidTransactionImporterImpl.class);

    @Autowired
    public PlaidTransactionImporterImpl(PlaidTransactionConverter plaidTransactionConverter,
                                        PlaidTransactionService plaidTransactionService,
                                        ExternalTransactionService externalTransactionService,
                                        PlaidLinkService plaidLinkService,
                                        TransactionCriteriaService transactionCriteriaService,
                                        TransactionStatementService transactionStatementService,
                                        ExternalAccountsService externalAccountsService,
                                        PlaidTransactionManagerImpl plaidTransactionManager) {
        super(externalAccountsService, plaidLinkService);
        this.plaidTransactionConverter = plaidTransactionConverter;
        this.plaidTransactionService = plaidTransactionService;
        this.externalTransactionService = externalTransactionService;
        this.plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        this.transactionToPlaidTransactionConverter = new TransactionToPlaidTransactionConverter();
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

    public List<PlaidTransaction> getPlaidTransactionsForPeriod(final LocalDate startDate, final LocalDate endDate, final int userId) throws IOException {
        if(startDate == null || endDate == null)
        {
            throw new IllegalArgumentException("startDate and endDate cannot be null");
        }
        TransactionsGetResponse transactionsGetResponse = getTransactionResponseFromServer(userId, startDate, endDate);
        try
        {
            List<Transaction> transactions = transactionsGetResponse.getTransactions();
            List<PlaidTransaction> plaidTransactionList = new ArrayList<>();
            for (Transaction transaction : transactions) {
                if(transaction != null)
                {
                    PlaidTransaction plaidTransaction = convertTransactionToPlaidTransaction(transaction);
                    plaidTransactionList.add(plaidTransaction);
                }
            }
            return plaidTransactionList;

        }catch(PlaidTransactionsResponseException e)
        {
            LOGGER.error("There was an error retrieving Plaid Transaction response from the server: {}", e.getMessage());
            return Collections.emptyList();

        }catch(Exception e)
        {
            LOGGER.error("There was an error retrieving plaid transactions: {}", e.getMessage());
            return Collections.emptyList();
        }

    }

    private PlaidTransaction convertTransactionToPlaidTransaction(Transaction transaction) {
        return transactionToPlaidTransactionConverter.convert(transaction);
    }

    public TransactionsGetResponse getTransactionResponseFromServer(int userId, LocalDate startDate, LocalDate endDate) throws IOException {
        return plaidTransactionManager.getTransactionResponse(userId, startDate, endDate);
    }

    public Boolean checkSinglePlaidTransactionIsLinked(final PlaidTransaction plaidTransaction) {
        if(plaidTransaction == null)
        {
            return false;
        }
        String plaidAcctID = plaidTransaction.getAccountId();

        // Step 1: Verify the PlaidAccountID in the transaction is linked to a system Account
        String transactionId = plaidTransaction.getTransactionId();
        Optional<ExternalTransactionEntity> externalTransactionEntity = externalTransactionService.findByTransactionId(transactionId);
        if(externalTransactionEntity.isPresent())
        {
            // This tells us that the transaction is linked to a system account
            ExternalAccountsEntity externalAccountsEntity = externalTransactionEntity.get().getExternalAccounts();
            if(externalAccountsEntity != null)
            {
                // Retrieve the map
                Map<String, Integer> sysAcctIDAndPlaidMap = getSingleSysAndPlaidAcctIdMap(externalAccountsEntity);
                if(sysAcctIDAndPlaidMap.isEmpty())
                {
                    return false;
                }
                Integer sysAcctID = externalAccountsEntity.getAccount().getAcctID();
                return sysAcctIDAndPlaidMap.containsKey(plaidAcctID) && sysAcctIDAndPlaidMap.containsValue(sysAcctID);
            }
        }
        return false;
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
        if(user == null || plaidTransactionList == null)
        {
            throw new IllegalArgumentException("User or plaidTransactionList is null");
        }

        if(plaidTransactionList.isEmpty())
        {
            return Collections.emptyList();
        }

        List<AccountEntity> userAccounts = user.getAccounts().stream().toList();
        int maxLoop = Math.max(plaidTransactionList.size(), userAccounts.size());

        for(int i = 0; i < maxLoop; i++)
        {
            PlaidTransaction plaidTransaction = plaidTransactionList.get(i);
            AccountEntity accountEntity = userAccounts.get(i);

            if(!checkSinglePlaidTransactionIsLinked(plaidTransaction))
            {
                LinkedTransactionInfo linkedTransactionInfo = linkTransaction(plaidTransaction, accountEntity);
                addLinkedTransactionInfoToList(linkedTransactionsList, linkedTransactionInfo);
            }
        }
        return linkedTransactionsList;
    }

    public void addLinkedTransactionInfoToList(List<LinkedTransactionInfo> linkedTransactionInfoList, LinkedTransactionInfo linkedTransactionInfo) {
        linkedTransactionInfoList.add(linkedTransactionInfo);
    }

    public LinkedTransactionInfo linkTransaction(PlaidTransaction plaidTransaction, AccountEntity account)
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
