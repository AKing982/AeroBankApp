package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PlaidAccountToSystemAccountMapperImpl implements PlaidAccountToSystemAccountMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountToSystemAccountMapperImpl.class);
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
    public PlaidAccountToSystemAccountMapperImpl(AccountService accountService,
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
        Iterator<AccountCodeEntity> accountCodeEntityIterator = accountCodeEntities.iterator();

        for(PlaidAccount plaidAccount : plaidAccounts)
        {
            if(plaidAccount != null)
            {
                while(accountCodeEntityIterator.hasNext())
                {
                    AccountCodeEntity accountCode = accountCodeEntityIterator.next();
                    if(accountCode != null)
                    {
                        String plaidAccountSubType = plaidAccount.getSubtype();
                        if(!plaidAccountSubType.isEmpty())
                        {
                            int acctID = accountCode.getAccount_segment();
                            String externalAcctID = plaidAccount.getAccountId();
                            if(plaidAccountSubType.equals(CHECKING) || plaidAccountSubType.equals(CHECKING.toLowerCase()))
                            {

                                LinkedAccountInfo linkedAccountInfo = createLinkedAccountInfo(acctID, externalAcctID);
                                linkedAccountInfos.add(linkedAccountInfo);
                            }
                            else if(plaidAccountSubType.equals(SAVING))
                            {
                                LinkedAccountInfo linkedAccountInfo = createLinkedAccountInfo(acctID, externalAcctID);
                                linkedAccountInfos.add(linkedAccountInfo);
                            }
                        }
                        else
                        {
                            throw new IllegalArgumentException("PlaidAccount subtype cannot be null");
                        }
                    }
                }
            }
        }
//        Map<Integer, List<LinkedAccountInfo>> accountIdsMap = new HashMap<>();
//        assertPlaidAccountListIsNull(plaidAccounts);
//        if (plaidAccounts.isEmpty())
//        {
//            return new HashMap<>();
//        }
//        int userId = getUserIdFromUserEntity(user);
//
//        List<AccountCodeEntity> accountCodeEntities = getAccountCodesForUserId(userId);
//
//        for (PlaidAccount plaidAccount : plaidAccounts)
//        {
//            for(AccountCodeEntity accountCodeEntity : accountCodeEntities)
//            {
//                if(plaidAccount != null && accountCodeEntity != null)
//                {
//                    String subType = plaidAccount.getSubtype();
//                    if(!subType.isEmpty())
//                    {
//                        String externalId = plaidAccount.getAccountId();
//                        if(subType.equals(CHECKING) || subType.equals(CHECKING.toLowerCase()))
//                        {
//                            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
//                            if(accountType.equals(CHECKING_CODE))
//                            {
//                                // Create an ExternalAccountsEntity with the accountId and plaid accountId
//                                int acctID = getAccountSegmentFromAccountCodeEntity(accountCodeEntity);
//                                LinkedAccountInfo linkedAccountInfo = createLinkedAccountInfo(acctID, externalId);
//                                setAccountsIdsMap(accountIdsMap, userId, linkedAccountInfo);
//                            }
//                        }
//                        else if(subType.equals(SAVING) || subType.equals(SAVING.toLowerCase()))
//                        {
//                            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
//                            if(accountType.equals(SAVINGS_CODE))
//                            {
//                                // Create an ExternalAccountsEntity with the accountId and plaid accountId
//                                int acctID = getAccountSegmentFromAccountCodeEntity(accountCodeEntity);
//                                LinkedAccountInfo linkedAccountInfo = createLinkedAccountInfo(acctID, externalId);
//                                setAccountsIdsMap(accountIdsMap, userId, linkedAccountInfo);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return accountIdsMap;
        return linkedAccountInfos;
    }

    public Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap)
    {
//        try
//        {
//            for(Map.Entry<Integer, List<LinkedAccountInfo>> entry : accountIdsMap.entrySet())
//            {
//                List<LinkedAccountInfo> linkedAccountInfos = entry.getValue();
//                for(LinkedAccountInfo linkedAccountInfo : linkedAccountInfos)
//                {
//                    int systemAcctID = linkedAccountInfo.getSystemAcctID();
//                    String externalAcctID = linkedAccountInfo.getExternalAcctID();
//                    createAndSaveExternalAccountEntity(externalAcctID,systemAcctID);
//                }
//            }
//            return true;
//        }catch(Exception e)
//        {
//            LOGGER.error("There was an error creating and saving the external account entity: ", e);
//            return false;
//        }
        return null;
    }


    private int getAccountSegmentFromAccountCodeEntity(AccountCodeEntity accountCodeEntity) {
        return accountCodeEntity.getAccount_segment();
    }

    private void createAndSaveExternalAccountEntity(String externalAcctID, int acctID)
    {
        ExternalAccountsEntity externalAccountsEntity = externalAccountsService.createExternalAccount(externalAcctID, acctID);
        externalAccountsService.save(externalAccountsEntity);
    }

    private String getAccountTypeFromAccountCodeEntity(AccountCodeEntity accountCodeEntity)
    {
        return accountCodeEntity.getAccountType();
    }

    private List<AccountCodeEntity> getAccountCodesForUserId(int userId) {
        return accountCodeService.getAccountCodesListByUserID(userId);
    }

    private Optional<AccountCodeEntity> getAccountCodeEntityByUserIdAndAcctId(final int userId, int acctID)
    {
        return accountCodeService.findByUserIdAndAcctSegment(userId, acctID);
    }

    private LinkedAccountInfo createLinkedAccountInfo(int acctID, String externalAcctID)
    {
        return new LinkedAccountInfo(acctID, externalAcctID);
    }

    public void setAccountsIdsMap(Map<Integer, List<LinkedAccountInfo>> accountsIdsMap, int userId, LinkedAccountInfo linkedAccountInfo) {
        accountsIdsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(linkedAccountInfo);
    }

    private List<AccountEntity> getUserSystemAccountsByUserId(int userId){
        return accountService.findByUserId(userId);
    }

    private void assertPlaidAccountListIsNull(final List<PlaidAccount> plaidAccounts)
    {
        if(plaidAccounts == null)
        {
            throw new IllegalArgumentException("plaidAccounts cannot be null");
        }
    }


    private int getUserIdFromUserEntity(UserEntity user)
    {
        return user.getUserID();
    }
}
