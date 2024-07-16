package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
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

@Component
public class PlaidAccountToSystemAccountMapperImpl implements PlaidAccountToSystemAccountMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountToSystemAccountMapperImpl.class);
    private final AccountService accountService;
    private final AccountCodeService accountCodeService;
    private final ExternalAccountsService externalAccountsService;
    private final String CHECKING = "CHECKING";
    private final String SAVING = "SAVING";
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
    public Map<Integer, List<String>> getUserToAccountIdsMap(UserEntity user, List<PlaidAccount> plaidAccounts) {
        Map<Integer, List<String>> accountIdsMap = new HashMap<>();
        assertPlaidAccountListIsNull(plaidAccounts);
        if (plaidAccounts.isEmpty())
        {
            return new HashMap<>();
        }
        int userId = getUserIdFromUserEntity(user);

        List<AccountCodeEntity> accountCodeEntities = getAccountCodesForUserId(userId);

        for (PlaidAccount plaidAccount : plaidAccounts)
        {
            for(AccountCodeEntity accountCodeEntity : accountCodeEntities)
            {
                if(plaidAccount != null && accountCodeEntity != null)
                {
                    String subType = plaidAccount.getSubtype();
                    if(!subType.isEmpty())
                    {
                        String externalId = plaidAccount.getAccountId();
                        if(subType.equals(CHECKING) || subType.equals(CHECKING.toLowerCase()))
                        {
                            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
                            if(accountType.equals(CHECKING_CODE))
                            {
                                // Create an ExternalAccountsEntity with the accountId and plaid accountId
                                int acctID = getAccountSegmentFromAccountCodeEntity(accountCodeEntity);
                                createAndSaveExternalAccountEntity(externalId, acctID);
                                setAccountsIdsMap(accountIdsMap, userId, externalId);
                            }
                        }
                        else if(subType.equals(SAVING) || subType.equals(SAVING.toLowerCase()))
                        {
                            String accountType = getAccountTypeFromAccountCodeEntity(accountCodeEntity);
                            if(accountType.equals(SAVINGS_CODE))
                            {
                                // Create an ExternalAccountsEntity with the accountId and plaid accountId
                                int acctID = getAccountSegmentFromAccountCodeEntity(accountCodeEntity);
                                createAndSaveExternalAccountEntity(externalId, acctID);
                                setAccountsIdsMap(accountIdsMap, userId, externalId);
                            }
                        }
                    }
                }
            }
        }
        return accountIdsMap;
    }

    public void updateAccountWithExternalAcctID(Map<Integer, List<String>> accountIdsMap) {
        for(Map.Entry<Integer, List<String>> entry : accountIdsMap.entrySet())
        {
            
        }
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

    public void setAccountsIdsMap(Map<Integer, List<String>> accountsIdsMap, int userId, String externalAcctID) {
        accountsIdsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(externalAcctID);
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
