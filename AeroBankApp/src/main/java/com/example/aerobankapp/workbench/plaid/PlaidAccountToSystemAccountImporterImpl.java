package com.example.aerobankapp.workbench.plaid;


import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
import com.example.aerobankapp.workbench.data.UserDataManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.convertPlaidSubTypeEnumListToStrings;

@Component
public class PlaidAccountToSystemAccountImporterImpl implements PlaidAccountToSystemAccountImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountToSystemAccountImporterImpl.class);
    private final AccountService accountService;
    private final AccountCodeService accountCodeService;
    private final UserDataManagerImpl userDataManager;
    private final ExternalAccountsService externalAccountsService;

    @Autowired
    public PlaidAccountToSystemAccountImporterImpl(AccountService accountService,
                                                 AccountCodeService accountCodeService,
                                                 UserDataManagerImpl userDataManager,
                                                 ExternalAccountsService externalAccountsService) {
        this.accountService = accountService;
        this.accountCodeService = accountCodeService;
        this.userDataManager = userDataManager;
        this.externalAccountsService = externalAccountsService;
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

    public Boolean importPlaidDataToSystemAccount(final PlaidAccount plaidAccount, final Account account){
        if(plaidAccount == null || account == null){
            LOGGER.error("PlaidAccount cannot be null");
            throw new IllegalArgumentException("Cannot import plaid account data due to null account data");
        }
        try{
            setSystemAccountProperties(account, plaidAccount);

            // Update the Account table with the modified properties

            return true;
        }catch(Exception e){
            LOGGER.error("Failed to import plaid account data for user: {}, due to the error: {}", account.getUserID(), e.getMessage());
            return false;
        }
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

    public boolean validateSubTypeMatchesType(String subType, String type)
    {
        if(subType.equals("checking") && type.equals("depository")){
            return true;
        }else if(subType.equals("savings") && type.equals("depository")){
            return true;
        }else if(subType.equals("paypal") && type.equals("credit")){
            return true;
        }else if(subType.equals("student") && type.equals("loan")){
            return true;
        }else if(subType.equals("auto") && type.equals("loan")){
            return true;
        }else if(subType.equals("personal") && type.equals("loan")){
            return true;
        }else if(subType.equals("mortgage") && type.equals("loan")){
            return true;
        }else return subType.equals("payable") && type.equals("loan");
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

    private UserEntity getUserEntityByUserId(int userId){
        return userDataManager.findUser(userId);
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
