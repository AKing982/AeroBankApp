package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
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
    private final ExternalAccountsService externalAccountsService;

    @Autowired
    public PlaidAccountToSystemAccountImporterImpl(AccountService accountService,
                                                 AccountCodeService accountCodeService,
                                                 ExternalAccountsService externalAccountsService) {
        this.accountService = accountService;
        this.accountCodeService = accountCodeService;
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
