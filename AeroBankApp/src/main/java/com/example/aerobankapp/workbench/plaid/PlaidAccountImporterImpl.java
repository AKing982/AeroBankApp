package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.repositories.ExternalAccountsRepository;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
import com.example.aerobankapp.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.convertPlaidSubTypeEnumListToStrings;

@Component
@Setter
@Getter
public class PlaidAccountImporterImpl extends AbstractPlaidDataImporter implements PlaidAccountImporter
{
    private final AccountService accountService;
    private final AccountCodeService accountCodeService;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountImporterImpl.class);
    private List<LinkedAccountInfo> linkedAccountInfoList = new ArrayList<>();

    @Autowired
    public PlaidAccountImporterImpl(ExternalAccountsService externalAccountsService,
                                    AccountService accountService,
                                    AccountCodeService accountCodeService,
                                    UserService userService) {
        super(externalAccountsService);
        this.accountService = accountService;
        this.accountCodeService = accountCodeService;
        this.userService = userService;
    }

    @Override
    public List<LinkedAccountInfo> prepareLinkedAccountInfoList(UserEntity user, List<PlaidAccount> plaidAccounts) {
        if(user == null || plaidAccounts == null)
        {
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
        if(accountCodeArrayLength == 0)
        {
            throw new RuntimeException("No accounts found for userID: " + userID);
        }
        int plaidAccountsArrayLength = plaidAccounts.size();
        if(plaidAccountsArrayLength == 0)
        {
            return Collections.emptyList();
        }
        int loopCount = Math.min(plaidAccounts.size(), accountEntities.size());
        for(int i = 0; i < loopCount; ++i) {
            PlaidAccount plaidAccount = plaidAccounts.get(i);
            AccountEntity accountEntity = accountEntities.get(i);

            if(plaidAccount != null && accountEntity != null){
                linkedAccountInfoList.add(linkAccounts(plaidAccount, accountEntity));
            }
        }
        // If there are still plaid accounts left over when we've run out
        // of Account entities
        if(loopCount < plaidAccounts.size())
        {
            // Do something else wiht the remaining plaid accounts
            for(int i = loopCount; i < plaidAccounts.size(); ++i) {
                PlaidAccount plaidAccount = plaidAccounts.get(i);
            }
        }
        return linkedAccountInfoList;
    }

    @Override
    public LinkedAccountInfo linkAccounts(final PlaidAccount plaidAccount, final AccountEntity accountEntity) {
        Set<String> plaidAccountSubtypes = convertPlaidSubTypeEnumListToStrings();
        LinkedAccountInfo linkedAccountInfo = null;
        String subType = plaidAccount.getSubtype();
        if(subType.isEmpty())
        {
            throw new IllegalArgumentException("Subtype cannot be null or empty");
        }

        // Check that the plaid sub type is supported
        if(plaidAccountSubtypes.contains(subType))
        {
            int acctID = accountEntity.getAcctID();
            LOGGER.info("Account System ID: " + acctID);
            String externalAcctID = plaidAccount.getAccountId();

            String accountSubType = accountEntity.getSubtype();
            String plaidAcctSubType = plaidAccount.getSubtype();
            if(accountSubType.equalsIgnoreCase(plaidAcctSubType))
            {
                LOGGER.info("Subtypes match");
                String accountType = getAccountTypeFromAccountEntity(accountEntity);
                if(accountType.equalsIgnoreCase(plaidAccount.getType()))
                {
                    LOGGER.info("Account Types match");
                    // Validate the Masks match
                    String sysMask = accountEntity.getMask();
                    linkedAccountInfo = buildLinkedAccountInfo(acctID, externalAcctID);
                }
            }
        }
        return linkedAccountInfo;
    }

    @Override
    public PlaidImportResult importDataFromPlaidAccountToSystemAccount(PlaidAccount plaidAccount, Account account) {
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

    public boolean validateAccountTypeStructure(String accountType)
    {
        return accountType.equals(PlaidLinkedAccountConstants.CHECKING_CODE) || accountType.equals(PlaidLinkedAccountConstants.SAVINGS_CODE)
                || accountType.equals(PlaidLinkedAccountConstants.AUTO_CODE) || accountType.equals(PlaidLinkedAccountConstants.PAYPAL_CODE) ||
                accountType.equals(PlaidLinkedAccountConstants.INVESTMENTS_CODE) || accountType.equals(PlaidLinkedAccountConstants.STUDENT_LOAN_CODE) ||
                accountType.equals(PlaidLinkedAccountConstants.CREDIT_CARD_CODE);
    }


    private String getAccountTypeFromAccountEntity(AccountEntity accountEntity)
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

    private LinkedAccountInfo buildLinkedAccountInfo(int acctID, String externalAcctID) {
        return createLinkedAccountInfo(acctID, externalAcctID);
    }

    private void addLinkedAccountToList(List<LinkedAccountInfo> linkedAccountInfos, LinkedAccountInfo linkedAccountInfo) {
        linkedAccountInfos.add(linkedAccountInfo);
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

    @Override
    public Boolean validateAccountSubTypeToTypeCriteria(List<AccountEntity> accountEntities) {
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

    @Override
    public Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap) {
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


}
