package com.example.aerobankapp.workbench.plaid;


import com.example.aerobankapp.converter.PlaidTransactionConverter;
import com.example.aerobankapp.converter.PlaidTransactionToTransactionCriteriaConverter;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.convertPlaidSubTypeEnumListToStrings;

@Component
public class PlaidDataImporterImpl implements PlaidDataImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidDataImporterImpl.class);
    private final AccountService accountService;
    private final AccountCodeService accountCodeService;
    private final UserServiceImpl userService;
    private final ExternalAccountsService externalAccountsService;
    private final ReferenceNumberGenerator referenceNumberGenerator;
    private final PlaidTransactionConverter plaidTransactionConverter;
    private final PlaidTransactionService plaidTransactionService;
    private final PlaidTransactionToTransactionCriteriaConverter plaidTransactionToTransactionCriteriaConverter;
    private final TransactionCriteriaService transactionCriteriaService;
    private final TransactionStatementService transactionStatementService;

    @Autowired
    public PlaidDataImporterImpl(AccountService accountService,
                                                 AccountCodeService accountCodeService,
                                                 UserServiceImpl userService,
                                                 ExternalAccountsService externalAccountsService,
                                                 PlaidTransactionService plaidTransactionService,
                                                 TransactionCriteriaService transactionCriteriaService,
                                                 TransactionStatementService transactionStatementService) {
        this.accountService = accountService;
        this.accountCodeService = accountCodeService;
        this.userService = userService;
        this.externalAccountsService = externalAccountsService;
        this.referenceNumberGenerator = new ReferenceNumberGeneratorImpl();
        this.plaidTransactionConverter = new PlaidTransactionConverter();
        this.plaidTransactionService = plaidTransactionService;
        this.plaidTransactionToTransactionCriteriaConverter = new PlaidTransactionToTransactionCriteriaConverter();
        this.transactionCriteriaService = transactionCriteriaService;
        this.transactionStatementService = transactionStatementService;
    }

    @Override
    public List<LinkedAccountInfo> getLinkedAccountInfoList(final UserEntity user, final List<PlaidAccount> plaidAccounts) {
        List<LinkedAccountInfo> linkedAccountInfos = new ArrayList<>();
        if(user == null || plaidAccounts == null) {
            throw new IllegalArgumentException("User cannot be null or PlaidAccounts cannot be null");
        }

        int userID = user.getUserID();
        if(userID < 1)
        {
            throw new InvalidUserIDException("Invalid UserId caught: " + userID);
        }
        List<AccountEntity> accountEntities = getAccountCodesForUserId(userID);

        //TODO: Fix issue when there's more accountCodes than plaid accounts
        int accountCodeArrayLength = accountEntities.size();
        int plaidAccountsArrayLength = plaidAccounts.size();
        if(accountCodeArrayLength > plaidAccountsArrayLength)
        {

        }

        int loopCount = Math.min(plaidAccounts.size(), accountEntities.size());
        for(int i = 0; i < loopCount; ++i) {
            PlaidAccount plaidAccount = plaidAccounts.get(i);
            AccountEntity accountEntity = accountEntities.get(i);

            if(plaidAccount != null && accountEntity != null){
                linkedAccountInfos.addAll(processPlaidAccountBySubType(plaidAccount, accountEntity));
            }
        }

        if(plaidAccounts.size() != accountEntities.size()){
            LOGGER.warn("Mismatch in count between Plaid accounts and account codes. Plaid accounts count: {} and account codes count: {}",
                    plaidAccounts.size(), accountEntities.size());
        }
        return linkedAccountInfos;
    }


    public List<LinkedAccountInfo> processPlaidAccountBySubType(final PlaidAccount plaidAccount, final AccountEntity accountCodeEntity) {
        List<LinkedAccountInfo> linkedAccountInfos = new ArrayList<>();
        Set<String> plaidAccountSubtypes = convertPlaidSubTypeEnumListToStrings();
        String subType = plaidAccount.getSubtype();
        if(subType.isEmpty())
        {
            throw new IllegalArgumentException("Subtype cannot be null or empty");
        }

        // Check that the plaid sub type is supported
        if(plaidAccountSubtypes.contains(subType))
        {
            int acctID = accountCodeEntity.getAcctID();
            LOGGER.info("Account System ID: " + acctID);
            String externalAcctID = plaidAccount.getAccountId();

            // Next match the sub type with the user's account
            // If the sub types are equal, then validate the account Type matches
            // standard account type

            // TODO: Add verification for AccountType
            // e.g. does the user's system checking account match the plaid account type DEPOSITORY, etc...

            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
            boolean accountTypeIsValid = validateAccountTypeStructure(accountType);
            if(accountTypeIsValid)
            {
                LinkedAccountInfo linkedAccountInfo = buildLinkedAccountInfo(acctID, externalAcctID);
                addLinkedAccountToList(linkedAccountInfos, linkedAccountInfo);
            }
        }
        return linkedAccountInfos;
    }

    public List<PlaidImportResult> performPlaidTransactionsMigration(final List<PlaidTransaction> plaidTransactions){
        List<PlaidImportResult> plaidImportResults = new ArrayList<>();
        if(plaidTransactions == null)
        {
            throw new IllegalArgumentException("Plaid transactions cannot be null");
        }

        if(plaidTransactions.isEmpty()){
            return List.of(createPlaidImportResult(null, false));
        }

        for(PlaidTransaction transaction : plaidTransactions){
            PlaidImportResult result = importPlaidTransactionToSystem(transaction);
            plaidImportResults.add(result);
        }

        for(PlaidImportResult result : plaidImportResults)
        {
            if(result != null && result.isSuccessful())
            {
                // Convert the plaid transaction to plaid transaction entity
                PlaidTransactionImport plaidTransaction = (PlaidTransactionImport) result.getResult();
                PlaidTransactionEntity plaidTransactionEntity = plaidTransactionConverter.convert(plaidTransaction);
                plaidTransactionService.save(plaidTransactionEntity);

                TransactionCriteriaEntity transactionCriteria = plaidTransactionToTransactionCriteriaConverter.convert(plaidTransaction);
                transactionCriteriaService.save(transactionCriteria);

            }
        }

        // store all the plaid transactions for the user into the PlaidTransactions table
        // create transaction criteria entity and store to database
        // create transaction schedule criteria and store to database
        // create and store transaction statements to the database
        // Determine which type of transaction this is and create transaction type
        // Create and initialize the TransactionScheduleCriteria

        return plaidImportResults;
    }

    public PlaidImportResult importPlaidTransactionToSystem(final PlaidTransaction plaidTransaction){
        if(plaidTransaction == null){
            throw new IllegalArgumentException("Plaid transaction cannot be null");
        }
        validatePlaidTransactionCriteria(plaidTransaction);

        // Details like Transaction Name, Amount, Date and pending go into the transaction criteria
        // If no reference number or confirmation number for the transaction, then generate both numbers
        // and store
        String transactionName = plaidTransaction.getTransactionName();
        BigDecimal amount = plaidTransaction.getAmount();
        boolean isPending = plaidTransaction.isPending();
        String transactionId = plaidTransaction.getTransactionId();
        String acctID = plaidTransaction.getAccountId();
        String referenceNumber = referenceNumberGenerator.generateReferenceNumber().getReferenceValue();
        LocalDate posted = plaidTransaction.getDate();
        LocalDate transactionDate = plaidTransaction.getDate();
        PlaidTransactionImport plaidTransactionImport = createPlaidTransactionImport(transactionId, transactionName, amount, isPending, referenceNumber,acctID, transactionDate, posted);
        return createPlaidImportResult(plaidTransactionImport, true);
    }

    private PlaidTransactionImport createPlaidTransactionImport(String transactionId,
                                                             String name,
                                                             BigDecimal amount,
                                                             boolean isPending,
                                                             String referenceNumber,
                                                             String acctID,
                                                             LocalDate transactionDate,
                                                             LocalDate posted){
        PlaidTransactionImport plaidTransactionImport = new PlaidTransactionImport();
        plaidTransactionImport.setTransactionId(transactionId);
        plaidTransactionImport.setTransactionDate(transactionDate);
        plaidTransactionImport.setAmount(amount);
        plaidTransactionImport.setTransactionName(name);
        plaidTransactionImport.setPending(isPending);
        plaidTransactionImport.setReferenceNumber(referenceNumber);
        plaidTransactionImport.setAcctID(acctID);
        plaidTransactionImport.setPosted(posted);
        return plaidTransactionImport;
    }

    private void validatePlaidTransactionCriteria(PlaidTransaction plaidTransaction)
    {
        if(plaidTransaction.getTransactionName() == null
                || plaidTransaction.getAccountId() == null || plaidTransaction.getAmount() == null
                || plaidTransaction.getDate() == null){
            LOGGER.error("Found null Plaid Transaction Criteria: {}", plaidTransaction.toString());
            throw new IllegalArgumentException("Plaid transaction criteria cannot be null");
        }
    }

    private PlaidImportResult createPlaidImportResult(Object result, boolean isSuccess){
        return new PlaidImportResult(result, isSuccess);
    }

    public PlaidImportResult importPlaidDataToSystemAccount(final PlaidAccount plaidAccount, final Account account)
    {
        if(plaidAccount == null || account == null)
        {
            LOGGER.error("PlaidAccount cannot be null");
            throw new IllegalArgumentException("Cannot import plaid account data due to null account data");
        }
        setSystemAccountProperties(account, plaidAccount);
        int userID = account.getUserID();
        PlaidImportResult result;

        LOGGER.info("UserID: {}", userID);
        LOGGER.info("AcctID: {}", account.getAccountID());
        UserEntity userEntity = userService.findById(userID);
        if(userEntity != null)
        {
            AccountCodeEntity accountCode = getAccountCodeByUserIdAndAcctID(userEntity.getUserID(), account.getAccountID());
            AccountEntity accountEntity = createAccountEntityFromModel(account, userEntity, accountCode);
            if(accountEntity == null)
            {
                throw new IllegalArgumentException("AccountEntity cannot be null");
            }
            else
            {
                saveAccountEntityToDatabase(accountEntity);
                result = createPlaidImportResult(account, true);
            }
        }
        else
        {
            LOGGER.warn("UserID: {} not found", userID);
            result = createPlaidImportResult(null, false);
        }
        return result;
    }

    private void saveAccountEntityToDatabase(AccountEntity accountEntity){
        accountService.save(accountEntity);
    }

    private AccountCodeEntity getAccountCodeByUserIdAndAcctID(int userId, int acctID){
        return accountCodeService.findByUserIdAndAcctSegment(userId, acctID);
    }

    private AccountEntity createAccountEntityFromModel(Account account, UserEntity user, AccountCodeEntity accountCodeEntity){
        return accountService.buildAccountEntityByAccountModel(account, accountCodeEntity, user);
    }

    private void setSystemAccountProperties(final Account account, final PlaidAccount plaidAccount){
        account.setType(plaidAccount.getType());
        account.setBalance(plaidAccount.getCurrentBalance());
        account.setAccountName(plaidAccount.getOfficialName());
    }

    public boolean validateAccountMaskAreEqual(String systemAcctMask, String plaidAcctMask){
        return systemAcctMask.equals(plaidAcctMask);
    }

    private Map<String, String> subTypeToTypeMap(){
        Map<String, String> subTypeToTypeCriteria = new HashMap<>();
        subTypeToTypeCriteria.put("checking", "depository");
        subTypeToTypeCriteria.put("savings", "depository");
        subTypeToTypeCriteria.put("paypal", "credit");
        subTypeToTypeCriteria.put("student", "loan");
        subTypeToTypeCriteria.put("auto", "loan");
        subTypeToTypeCriteria.put("personal", "loan");
        subTypeToTypeCriteria.put("mortgage", "loan");
        subTypeToTypeCriteria.put("payable", "loan");
        return subTypeToTypeCriteria;
    }

    public Boolean validateAccountSubTypeToTypeCriteria(final List<AccountEntity> accountEntities)
    {
        Map<String, String> subTypeToTypeCriteria = subTypeToTypeMap();
        if(accountEntities == null || accountEntities.isEmpty()){
            throw new IllegalArgumentException("Account List cannot be null or empty.");
        }

        for(AccountEntity accountEntity : accountEntities)
        {
            if(accountEntity != null)
            {
                String subtype = accountEntity.getSubtype();
                String type = accountEntity.getType();
                LOGGER.info("SubType: {}", subtype);
                LOGGER.info("Type: {}", type);
                if(subtype == null || type == null)
                {
                    throw new IllegalArgumentException("Subtype or type cannot be null.");
                }
                if(!type.equals(subTypeToTypeCriteria.get(subtype)))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateAccountTypeStructure(String accountType)
    {
        return accountType.equals(PlaidLinkedAccountConstants.CHECKING_CODE) || accountType.equals(PlaidLinkedAccountConstants.SAVINGS_CODE)
                || accountType.equals(PlaidLinkedAccountConstants.AUTO_CODE) || accountType.equals(PlaidLinkedAccountConstants.PAYPAL_CODE) ||
                accountType.equals(PlaidLinkedAccountConstants.INVESTMENTS_CODE) || accountType.equals(PlaidLinkedAccountConstants.STUDENT_LOAN_CODE) ||
                accountType.equals(PlaidLinkedAccountConstants.CREDIT_CARD_CODE);
    }


    private LinkedAccountInfo buildLinkedAccountInfo(int acctID, String externalAcctID) {
        return createLinkedAccountInfo(acctID, externalAcctID);
    }

    private void addLinkedAccountToList(List<LinkedAccountInfo> linkedAccountInfos, LinkedAccountInfo linkedAccountInfo) {
        linkedAccountInfos.add(linkedAccountInfo);
    }

    public Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap)
    {
        try
        {
            for(LinkedAccountInfo linkedAccountInfo : accountIdsMap)
            {
                int systemAcctID = linkedAccountInfo.getSystemAcctID();
                String externalAcctID = linkedAccountInfo.getExternalAcctID();
                createAndSaveExternalAccountEntity(externalAcctID,systemAcctID);
            }
            return true;
        }catch(Exception e)
        {
            LOGGER.error("There was an error creating and saving the external account entity: ", e);
            return false;
        }
    }


    private int getAccountSegmentFromAccountCodeEntity(AccountCodeEntity accountCodeEntity) {
        return accountCodeEntity.getAccount_segment();
    }

    private void createAndSaveExternalAccountEntity(String externalAcctID, int acctID)
    {
        ExternalAccountsEntity externalAccountsEntity = externalAccountsService.createExternalAccount(externalAcctID, acctID);
        LOGGER.info("External Account Entity: " + externalAccountsEntity.toString());
        externalAccountsService.save(externalAccountsEntity);
    }

    private String getAccountTypeFromAccountCodeEntity(AccountEntity accountEntity)
    {
        return accountEntity.getAccountType();
    }

    private List<AccountEntity> getAccountCodesForUserId(int userId) {
        return accountService.getListOfAccountsByUserID(userId);
    }

    private LinkedAccountInfo createLinkedAccountInfo(int acctID, String externalAcctID)
    {
        return new LinkedAccountInfo(acctID, externalAcctID);
    }
}
