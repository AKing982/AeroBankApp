package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.repositories.ExternalAccountsRepository;
import com.example.aerobankapp.services.*;
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
    private boolean isMatchedSubType;

    @Autowired
    public PlaidAccountImporterImpl(ExternalAccountsService externalAccountsService,
                                    PlaidLinkService plaidLinkService,
                                    AccountService accountService,
                                    AccountCodeService accountCodeService,
                                    UserService userService) {
        super(externalAccountsService, plaidLinkService);
        this.accountService = accountService;
        this.accountCodeService = accountCodeService;
        this.userService = userService;
    }

    @Override
    public List<LinkedAccountInfo> prepareLinkedAccounts(UserEntity user, List<PlaidAccount> plaidAccounts)
    {
        validateUserAndPlaidAccountList(user, plaidAccounts);
        validateUser(user);

        if(plaidAccounts == null)
        {
            throw new PlaidAccountNotFoundException("Plaid account not found");
        }

        if(plaidAccounts.isEmpty())
        {
            LOGGER.warn("No Plaid Accounts were found for userID: {}", user.getUserID());
            return Collections.emptyList();
        }

//        if(user == null || plaidAccounts == null)
//        {
//            throw new IllegalArgumentException("User cannot be null or PlaidAccounts cannot be null");
//        }
//        int userID = user.getUserID();
//        if(userID < 1)
//        {
//            throw new InvalidUserIDException("Invalid UserId caught: " + userID);
//        }
//        List<AccountEntity> accountEntities = getAccountCodesForUserId(userID);
//
//        //TODO: Fix issue when there's more accountCodes than plaid accounts
//        int accountCodeArrayLength = accountEntities.size();
//        if(accountCodeArrayLength == 0)
//        {
//            throw new RuntimeException("No accounts found for userID: " + userID);
//        }
//        int plaidAccountsArrayLength = plaidAccounts.size();
//        if(plaidAccountsArrayLength == 0)
//        {
//            return Collections.emptyList();
//        }
//        int loopCount = Math.min(plaidAccounts.size(), accountEntities.size());
//        for(int i = 0; i < loopCount; ++i) {
//            PlaidAccount plaidAccount = plaidAccounts.get(i);
//            AccountEntity accountEntity = accountEntities.get(i);
//
//            if(plaidAccount != null && accountEntity != null){
//                linkedAccountInfoList.add(linkAccounts(plaidAccount, accountEntity));
//            }
//        }
//        // If there are still plaid accounts left over when we've run out
//        // of Account entities
//        if(loopCount < plaidAccounts.size())
//        {
//            // Do something else wiht the remaining plaid accounts
//            for(int i = loopCount; i < plaidAccounts.size(); ++i) {
//                PlaidAccount plaidAccount = plaidAccounts.get(i);
//            }
//        }
//        return linkedAccountInfoList;
        return null;
    }

    private void validateUserAndPlaidAccountList(UserEntity user, List<PlaidAccount> plaidAccounts) {
        if(user == null && plaidAccounts == null)
        {
            throw new IllegalArgumentException("User and PlaidAccount cannot be null");
        }
    }

    @Override
    public LinkedAccountInfo linkAccounts(final PlaidAccount plaidAccount, final AccountEntity accountEntity) {

        if(plaidAccount == null && accountEntity == null) {
            throw new IllegalArgumentException("PlaidAccount and accountEntity must not be null");
        }
        assertPlaidAccountIsNull(plaidAccount);
        assertAccountIsNull(accountEntity);

        String plaidSubType = plaidAccount.getSubtype();
        if(plaidSubType.isEmpty())
        {
            throw new InvalidPlaidSubTypeException(plaidSubType);
        }
        Set<String> plaidAccountSubtypes = convertPlaidSubTypeEnumListToStrings();
        for(String subType : plaidAccountSubtypes)
        {
            if(subType.equalsIgnoreCase(plaidSubType))
            {
                setIsMatchedSubType(true);
                break;
            }
        }

        UserEntity user = getUserEntityByAccountEntity(accountEntity);

        //TODO: Should check if user has an access token before proceeding to import accounts
        if(!checkPlaidAccessToken(user))
        {
            throw new PlaidAccessTokenNotFoundException("Plaid access token not found for userID: " + user.getUserID());
        }

        if(isMatchedSubType())
        {
            int acctID = accountEntity.getAcctID();
            String externalAcctID = plaidAccount.getAccountId();
            String acctSubType = accountEntity.getSubtype();

            if(acctSubType.equalsIgnoreCase(plaidSubType))
            {
                String type = accountEntity.getType();
                String plaidAccountType = plaidAccount.getType();
                if(type.equalsIgnoreCase(plaidAccountType))
                {
                    String mask = accountEntity.getMask();
                    if(mask.equals(plaidAccount.getMask()))
                    {
                        return buildLinkedAccountInfo(acctID, externalAcctID);
                    }
                }
                else
                {
                    return createLinkedAccountInfo(0, "");
                }

            }
            else
            {
                return createLinkedAccountInfo(0, "");
            }

        }
        return null;
    }

    private UserEntity getUserEntityByAccountEntity(AccountEntity accountEntity)
    {
        return accountEntity.getUser();
    }

    private void setIsMatchedSubType(boolean isMatch)
    {
        isMatchedSubType = isMatch;
    }

    private void assertAccountIsNull(final AccountEntity accountEntity)
    {
        if(accountEntity == null)
        {
            throw new AccountNotFoundException("Account Not Found");
        }
    }

    private void assertPlaidAccountIsNull(PlaidAccount plaidAccount) {
        if(plaidAccount == null)
        {
            throw new PlaidAccountNotFoundException("Plaid Account Not Found");
        }

    }

    @Override
    public PlaidImportResult importDataFromPlaidAccountToSystemAccount(PlaidAccount plaidAccount, Account account) {
//        if(plaidAccount == null || account == null)
//        {
//            LOGGER.error("PlaidAccount cannot be null");
//            throw new IllegalArgumentException("Cannot import plaid account data due to null account data");
//        }
//        setSystemAccountProperties(account, plaidAccount);
//        int userID = account.getUserID();
//        PlaidImportResult result;
//
//        LOGGER.info("UserID: {}", userID);
//        LOGGER.info("AcctID: {}", account.getAccountID());
//        UserEntity userEntity = userService.findById(userID);
//        if(userEntity != null)
//        {
//            AccountCodeEntity accountCode = getAccountCodeByUserIdAndAcctID(userEntity.getUserID(), account.getAccountID());
//            AccountEntity accountEntity = createAccountEntityFromModel(account, userEntity, accountCode);
//            if(accountEntity == null)
//            {
//                throw new IllegalArgumentException("AccountEntity cannot be null");
//            }
//            else
//            {
//                saveAccountEntityToDatabase(accountEntity);
//                result = createPlaidImportResult(account, true);
//            }
//        }
//        else
//        {
//            LOGGER.warn("UserID: {} not found", userID);
//            result = createPlaidImportResult(null, false);
//        }
//        return result;
        return null;
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
//        if(accountEntities == null || accountEntities.isEmpty()){
//            throw new IllegalArgumentException("Account List cannot be null or empty.");
//        }
//
//        for(AccountEntity accountEntity : accountEntities)
//        {
//            if(accountEntity != null)
//            {
//                String subtype = accountEntity.getSubtype();
//                String type = accountEntity.getType();
//                LOGGER.info("SubType: {}", subtype);
//                LOGGER.info("Type: {}", type);
//                if(subtype == null || type == null)
//                {
//                    throw new IllegalArgumentException("Subtype or type cannot be null.");
//                }
//                if(!type.equals(subTypeToTypeCriteria.get(subtype)))
//                {
//                    return false;
//                }
//            }
//        }
//        return true;
        return null;
    }

    @Override
    public Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap) {
//        try
////        {
////            for(LinkedAccountInfo linkedAccountInfo : accountIdsMap)
////            {
////                int systemAcctID = linkedAccountInfo.getSystemAcctID();
////                String externalAcctID = linkedAccountInfo.getExternalAcctID();
////                createAndSaveExternalAccountEntity(externalAcctID,systemAcctID);
////            }
////            return true;
////        }catch(Exception e)
////        {
////            LOGGER.error("There was an error creating and saving the external account entity: ", e);
////            return false;
////        }
        return null;
    }


}
