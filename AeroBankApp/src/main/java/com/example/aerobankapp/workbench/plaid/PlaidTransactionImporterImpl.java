package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.ExternalAccountsService;
import com.example.aerobankapp.services.PlaidTransactionService;
import com.example.aerobankapp.services.TransactionCriteriaService;
import com.example.aerobankapp.services.TransactionStatementService;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.createPlaidTransactionImport;

@Component
public class PlaidTransactionImporterImpl extends AbstractPlaidDataImporter implements PlaidTransactionImporter
{
    private final PlaidTransactionConverter plaidTransactionConverter;
    private final PlaidTransactionService plaidTransactionService;
    private final PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;
    private final TransactionCriteriaService transactionCriteriaService;
    private final TransactionStatementService transactionStatementService;
    private final ReferenceNumberGenerator referenceNumberGenerator;
    private final Logger LOGGER = LoggerFactory.getLogger(PlaidTransactionImporterImpl.class);

    @Autowired
    public PlaidTransactionImporterImpl(PlaidTransactionConverter plaidTransactionConverter,
                                        PlaidTransactionService plaidTransactionService,
                                        TransactionCriteriaService transactionCriteriaService,
                                        TransactionStatementService transactionStatementService,
                                        ExternalAccountsService externalAccountsService) {
        super(externalAccountsService);
        this.plaidTransactionConverter = plaidTransactionConverter;
        this.plaidTransactionService = plaidTransactionService;
        this.plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        this.transactionCriteriaService = transactionCriteriaService;
        this.transactionStatementService = transactionStatementService;
        this.referenceNumberGenerator = new ReferenceNumberGeneratorImpl();
        this.externalAccountsService = externalAccountsService;
    }

    @Override
    public List<PlaidTransactionImport> importPlaidTransactions(List<PlaidTransaction> plaidTransactionList) {
        if(plaidTransactionList == null)
        {
            throw new IllegalArgumentException("plaidTransactionList cannot be null");
        }

        List<PlaidTransactionImport> plaidTransactionImportList = new ArrayList<>();

        // Loop through the list of PlaidTransactions
        for(PlaidTransaction plaidTransaction : plaidTransactionList)
        {
            if(plaidTransaction != null)
            {
                PlaidImportResult plaidImportResult = processPlaidTransaction(plaidTransaction);
                if(plaidImportResult != null)
                {
                    PlaidTransactionImport plaidTransactionImport = (PlaidTransactionImport) plaidImportResult.getResult();
                    plaidTransactionImportList.add(plaidTransactionImport);
                }
            }
        }
        return plaidTransactionImportList;
    }

    @Override
    public PlaidImportResult processPlaidTransaction(PlaidTransaction plaidTransaction) {
        if(plaidTransaction == null){
            throw new IllegalArgumentException("plaidTransaction cannot be null");
        }

        // Generate the ReferenceNumber
        String referenceNumber = referenceNumberGenerator.generateReferenceNumber().getReferenceValue();
        PlaidTransactionImport plaidTransactionImport = createPlaidTransactionImport(plaidTransaction, referenceNumber);

        return createPlaidImportResult(plaidTransactionImport, true);
    }

    private TransactionStatementEntity prepareTransactionStatementEntityFromAmount(AccountEntity accountEntity, PlaidTransactionImport plaidTransactionImport) {
        // Convert to a string
        String amountStr = plaidTransactionImport.getAmount().toString();
        TransactionStatementEntity transactionStatementEntity;
        String transactionName = plaidTransactionImport.getTransactionName();
        BigDecimal amount = plaidTransactionImport.getAmount();
        if(amountStr.startsWith("-"))
        {
            // put the amount in the debit field
            transactionStatementEntity = createTransactionStatementEntity(accountEntity, transactionName, amount, null, null);
        }
        else
        {
            // put the amount in the credit field
            transactionStatementEntity = createTransactionStatementEntity(accountEntity, transactionName, null, amount, null);
        }
        return transactionStatementEntity;
    }

    private TransactionStatementEntity createTransactionStatementEntity(AccountEntity accountEntity, String descr, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
        return transactionStatementService.createTransactionStatementEntity(accountEntity, descr, debit, credit, balance);
    }

    @Override
    public Boolean saveProcessedPlaidTransactions(List<PlaidTransactionImport> plaidTransactionList) {
        try
        {
            for(PlaidTransactionImport plaidTransactionImport : plaidTransactionList)
            {
                if(plaidTransactionImport != null)
                {
                    createAndSavePlaidTransactionEntity(plaidTransactionImport);
                    createAndSaveTransactionCriteriaEntity(plaidTransactionImport);
                    createAndSaveTransactionStatement(plaidTransactionImport);
                }
            }
            return true;
        }catch(Exception ex)
        {
            LOGGER.error("There was an error processing the plaid transaction imports: {}", ex.getMessage());
            return false;
        }
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
