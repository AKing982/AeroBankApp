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

@Component
public class PlaidAccountToSystemAccountImporterImpl implements PlaidAccountToSystemAccountImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountToSystemAccountImporterImpl.class);
    private final AccountService accountService;
    private final AccountCodeService accountCodeService;
    private final ExternalAccountsService externalAccountsService;
    private final String CHECKING = "CHECKING";
    private final String SAVING = "SAVINGS";
    private final String RENT = "RENT";
    private final String INVESTMENT = "INVESTMENT";
    private final String CHECKING_CODE = "01";
    private final String SAVINGS_CODE = "02";
    private final String RENTS_CODE = "03";
    private final String INVESTMENTS_CODE = "04";

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
        List<AccountCodeEntity> accountCodeEntities = getAccountCodesForUserId(userID);

        //TODO: Fix issue when there's more accountCodes than plaid accounts
        int loopCount = Math.min(plaidAccounts.size(), accountCodeEntities.size());
        for(int i = 0; i < loopCount; ++i) {
            PlaidAccount plaidAccount = plaidAccounts.get(i);
            AccountCodeEntity accountCodeEntity = accountCodeEntities.get(i);

            if(plaidAccount != null && accountCodeEntity != null){
                linkedAccountInfos.addAll(processPlaidAccountBySubType(plaidAccount, accountCodeEntity));
            }
        }

        if(plaidAccounts.size() != accountCodeEntities.size()){
            LOGGER.warn("Mismatch in count between Plaid accounts and account codes. Plaid accounts count: {} and account codes count: {}",
                    plaidAccounts.size(), accountCodeEntities.size());
        }
        return linkedAccountInfos;
    }

    public List<LinkedAccountInfo> processPlaidAccountBySubType(final PlaidAccount plaidAccount, final AccountCodeEntity accountCodeEntity) {
        List<LinkedAccountInfo> linkedAccountInfos = new ArrayList<>();
        Set<String> validSubTypes = new HashSet<>(Arrays.asList(
                PlaidLinkedAccountConstants.CHECKING,
                PlaidLinkedAccountConstants.CHECKING.toLowerCase(),
                PlaidLinkedAccountConstants.SAVINGS
//                PlaidLinkedAccountConstants.SAVINGS.toLowerCase()
        ));

        String subType = plaidAccount.getSubtype();
        if(subType.isEmpty())
        {
            throw new IllegalArgumentException("Subtype cannot be null or empty");
        }

        if(validSubTypes.contains(subType))
        {
            int acctID = accountCodeEntity.getAccount_segment();
            LOGGER.info("Account System ID: " + acctID);
            String externalAcctID = plaidAccount.getAccountId();
            LOGGER.info("External Account ID: " + externalAcctID);
            LinkedAccountInfo linkedAccountInfo = buildLinkedAccountInfo(acctID, externalAcctID);
            addLinkedAccountToList(linkedAccountInfos, linkedAccountInfo);
        }

//        if(!subType.isEmpty() && validSubTypes.contains(subType))
//        {
//            int acctID = accountCodeEntity.getAccount_segment();
//            LOGGER.info("Account System ID: " + acctID);
//            String externalAcctID = plaidAccount.getAccountId();
//            LOGGER.info("External Account ID: " + externalAcctID);
//
//            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
//            if(accountType.equals(CHECKING_CODE) || accountType.equals(SAVINGS_CODE) || accountType.equals(RENTS_CODE))
//            {
//                LinkedAccountInfo linkedAccountInfo = buildLinkedAccountInfo(acctID, externalAcctID);
//                addLinkedAccountToList(linkedAccountInfos, linkedAccountInfo);
//            }
//        }
//        else
//        {
//            throw new IllegalArgumentException("PlaidAccount subtype cannot be null or not supported");
//        }
//        return linkedAccountInfos;
        return linkedAccountInfos;
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

    private String getAccountTypeFromAccountCodeEntity(AccountCodeEntity accountCodeEntity)
    {
        return accountCodeEntity.getAccountType();
    }

    private List<AccountCodeEntity> getAccountCodesForUserId(int userId) {
        return accountCodeService.getAccountCodesListByUserID(userId);
    }

    private LinkedAccountInfo createLinkedAccountInfo(int acctID, String externalAcctID)
    {
        return new LinkedAccountInfo(acctID, externalAcctID);
    }
}
