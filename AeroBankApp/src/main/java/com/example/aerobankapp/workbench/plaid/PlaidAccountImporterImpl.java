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
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.AccountBuilder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.convertPlaidSubTypeEnumListToStrings;

@Component
@Setter
@Getter
public class PlaidAccountImporterImpl extends AbstractPlaidDataImporter implements PlaidAccountImporter
{
    private final AccountBuilder accountBuilder;
    private final AccountCodeService accountCodeService;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(PlaidAccountImporterImpl.class);
    private List<LinkedAccountInfo> linkedAccountInfoList = new ArrayList<>();

    private Map<Integer, List<PlaidAccount>> unlinkedPlaidAccounts = new HashMap<>();
    private boolean isMatchedSubType;
    private boolean isLinked;

    @Autowired
    public PlaidAccountImporterImpl(ExternalAccountsService externalAccountsService,
                                    PlaidLinkService plaidLinkService,
                                    AccountService accountService,
                                    AccountBuilder accountBuilder,
                                    AccountCodeService accountCodeService,
                                    UserService userService) {
        super(externalAccountsService,accountService, plaidLinkService);
        this.accountBuilder = accountBuilder;
        this.accountCodeService = accountCodeService;
        this.userService = userService;
    }

    // TODO: Determine bug that causes more linked accounts to appear than actual linked accounts
    // TODO: In this case if two plaid accounts are found then there are 8 linked accounts
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
        int userID = user.getUserID();
        List<AccountEntity> accountEntities = getAccountsForUserId(userID);
        int loopCount = Math.max(plaidAccounts.size(), accountEntities.size());

        // If the plaid accounts size is greater than the account list
        try
        {
            for(int i = 0; i < loopCount; i++)
            {
                PlaidAccount plaidAccount = i < plaidAccounts.size() ? plaidAccounts.get(i) : null;
                AccountEntity accountEntity = i < accountEntities.size() ? accountEntities.get(i) : null;
                if(plaidAccount != null && accountEntity != null)
                {
                    addLinkedAccountInfoToList(linkedAccountInfoList, linkAccounts(plaidAccount, accountEntity));
                }
            }
        }catch(IndexOutOfBoundsException e)
        {
            LOGGER.error("Error while linking accounts", e);
        }
        // If there are still plaid accounts left over when we've run out
        // of Account entities
        addUnlinkedPlaidAccountsToMap(loopCount, user, plaidAccounts);
        return linkedAccountInfoList;
    }

    public void addUnlinkedPlaidAccountsToMap(int loopCount, UserEntity user, List<PlaidAccount> plaidAccounts)
    {
        if(loopCount < plaidAccounts.size())
        {
            // Do something else wiht the remaining plaid accounts
            for(int i = loopCount; i < plaidAccounts.size(); ++i) {
                PlaidAccount plaidAccount = plaidAccounts.get(i);
                unlinkedPlaidAccounts.computeIfAbsent(user.getUserID(), k -> new ArrayList<>()).add(plaidAccount);
            }
        }
    }

    public void handleLinkedAccountsWithEmptyPlaidAcctID(final List<LinkedAccountInfo> linkedAccountInfos)
    {
        for(LinkedAccountInfo linkedAccountInfo : linkedAccountInfos)
        {
            if(linkedAccountInfo != null)
            {
                int sysAcctID = linkedAccountInfo.getSystemAcctID();
                String plaidAcctID = linkedAccountInfo.getExternalAcctID();
                if(sysAcctID > 0 && plaidAcctID.isEmpty())
                {

                }
            }
        }
    }

    private void addLinkedAccountInfoToList(List<LinkedAccountInfo> list, LinkedAccountInfo linkedAccountInfo){
        list.add(linkedAccountInfo);
    }

    private void validateUserAndPlaidAccountList(UserEntity user, List<PlaidAccount> plaidAccounts) {
        if(user == null && plaidAccounts == null)
        {
            throw new IllegalArgumentException("User and PlaidAccount cannot be null");
        }
    }

    @Override
    public LinkedAccountInfo linkAccounts(final PlaidAccount plaidAccount, final AccountEntity accountEntity)
    {

        validateInputs(plaidAccount, accountEntity);
        processSubTypeValidation(plaidAccount);

        UserEntity user = getUserEntityByAccountEntity(accountEntity);
        if(!isMatchedSubType() && !checkPlaidAccessToken(user))
        {
            return null;
        }

        return processAccountLink(plaidAccount, accountEntity);
    }

    private void validateInputs(PlaidAccount plaidAccount, AccountEntity accountEntity)
    {
        if(plaidAccount == null && accountEntity == null)
        {
            throw new IllegalArgumentException("PlaidAccount cannot be null");
        }
        assertPlaidAccountIsNull(plaidAccount);
        assertAccountIsNull(accountEntity);
    }


    private LinkedAccountInfo processAccountLink(PlaidAccount plaidAccount, AccountEntity account)
    {
        int sysAcctID = account.getAcctID();
        String externalAcctID = plaidAccount.getAccountId();
        String accountSubtype = account.getSubtype();
        String plaidSubType = plaidAccount.getSubtype();

        if(!accountSubtype.equalsIgnoreCase(plaidSubType))
        {
            return buildLinkedAccountInfo(sysAcctID, "");
        }

        if(!account.getType().equalsIgnoreCase(plaidAccount.getType()))
        {
            return buildLinkedAccountInfo(sysAcctID, "");
        }

        if(!account.getMask().equalsIgnoreCase(plaidAccount.getMask()))
        {
            return buildLinkedAccountInfo(account.getAcctID(), "");
        }
        return buildLinkedAccountInfo(sysAcctID, externalAcctID);
    }

    private void processSubTypeValidation(PlaidAccount plaidAccount)
    {
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
    public PlaidImportResult importDataFromPlaidAccountToSystemAccount(final PlaidAccount plaidAccount, final Account account)
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
            if(accountCode == null)
            {
                throw new IllegalArgumentException("Account Code Not Found");
            }
            Optional<AccountEntity> accountEntityOptional = createAccountEntityFromModel(account, userEntity, accountCode);
            if(accountEntityOptional.isEmpty())
            {
                throw new IllegalArgumentException("AccountEntity cannot be null");
            }
            else
            {
                AccountEntity accountEntity = accountEntityOptional.get();
                updateAccountEntity(accountEntity);
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

    private List<AccountEntity> getAccountsForUserId(int userId) {
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


    private void updateAccountEntity(AccountEntity accountEntity){
        String name = accountEntity.getAccountName();
        BigDecimal balance = accountEntity.getBalance();
        String mask = accountEntity.getMask();
        int acctID = accountEntity.getAcctID();
        accountService.updateAccountEntityByNameAndBalanceAndMask(name, balance, mask, acctID);
    }

    public AccountCodeEntity getAccountCodeByUserIdAndAcctID(int userId, int acctID){
        return accountCodeService.findByUserIdAndAcctSegment(userId, acctID);
    }

    public Optional<AccountEntity> createAccountEntityFromModel(Account account, UserEntity user, AccountCodeEntity accountCodeEntity){
        if(account == null || user == null || accountCodeEntity == null)
        {
            throw new IllegalArgumentException("Cannot create account entity due to null account data");
        }
        AccountEntity accountEntity = accountService.buildAccountEntityByAccountModel(account, accountCodeEntity, user);
        if(accountEntity == null)
        {
            throw new IllegalArgumentException("Unable to build AccountEntity from model");
        }
        return Optional.of(accountEntity);
//        return accountService.buildAccountEntityByAccountModel(account, accountCodeEntity, user);
    }

    private void setSystemAccountProperties(final Account account, final PlaidAccount plaidAccount){
        account.setType(plaidAccount.getType());
        account.setBalance(plaidAccount.getCurrentBalance());
        account.setAccountName(plaidAccount.getName());
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
        if(accountIdsMap == null)
        {
            throw new LinkedAccountInfoListNullException("Linked Account Info List is null");
        }
        LOGGER.info("Linked Account Info list size: {}", accountIdsMap.size());

        try
        {
            LOGGER.info("Entering loop of LinkedAccountInfos");
            for(LinkedAccountInfo linkedAccountInfo : accountIdsMap)
            {
                if(linkedAccountInfo != null)
                {
                    LOGGER.info("LinkedAccountInfo is not null");
                    int systemAcctID = linkedAccountInfo.getSystemAcctID();
                    String externalAcctID = linkedAccountInfo.getExternalAcctID();
                    LOGGER.info("SystemAcctID: {}", systemAcctID);
                    LOGGER.info("ExternalAcctID: {}", externalAcctID);
                    LOGGER.info("Creating ExternalAccountEntity");
                    createAndSaveExternalAccountEntity(externalAcctID, systemAcctID);
                }
                LOGGER.info("LinkedAccountInfo is found null");
            }
            return true;
        }catch(Exception e)
        {
            LOGGER.error(String.format("There was an error creating and saving the external account entity: %s - %s ", e.getClass().getSimpleName(), e.getMessage()), e);
            return false;
        }
    }


    /**
     * Creates imported accounts from a non-linked Plaid accounts list.
     *
     * @param unlinkedPlaidAccounts A map containing user IDs mapped to a list of Plaid accounts that are not linked.
     * @throws IllegalArgumentException if unlinkedPlaidAccounts is null.
     */
    @Override
    public void createImportedAccountsFromNonLinkAccountsList(final Map<Integer, List<PlaidAccount>> unlinkedPlaidAccounts) {
        if(unlinkedPlaidAccounts == null)
        {
            throw new IllegalArgumentException("Unlinked Plaid Accounts map is null");
        }

        for(Map.Entry<Integer, List<PlaidAccount>> entry : unlinkedPlaidAccounts.entrySet())
        {
            int userID = entry.getKey();
            List<PlaidAccount> plaidAccounts = entry.getValue();
            UserEntity user = userService.findById(userID);

            // Retrieve the list of accounts for the user
            List<AccountEntity> accountEntities = getAccountListByUser(user);

            for(PlaidAccount plaidAccount : plaidAccounts)
            {
                // Validate the plaid account is unlinked
                // if the plaid account is not linked, then proceed
                if(!checkSinglePlaidAccountIsLinked(plaidAccount))
                {
                    // Create the System Account template through the AccountEntity
                    accountBuilder.createAccountEntityFromPlaidAccount(plaidAccount, user);
                    // Persist the System Account Entity to the database
                }

            }
        }
    }

    @Override
    public Map<Integer, List<PlaidAccount>> getPlaidAccountsMap() {
        return unlinkedPlaidAccounts;
    }


    /**
     * Retrieves the mapping of external account IDs to system account IDs.
     *
     * @param externalAccountsEntities The list of external accounts entities.
     * @return A map containing the external account ID as the key and the system account ID as the value.
     * @throws IllegalArgumentException if externalAccountsEntities is null or empty.
     */
    public Map<String, Integer> getSysAndExternalAcctIDs(final List<ExternalAccountsEntity> externalAccountsEntities)
    {
        if(externalAccountsEntities == null)
        {
            throw new IllegalArgumentException("External Accounts List cannot be null or empty.");
        }
        Map<String, Integer> pairedAcctIds = new LinkedHashMap<>();
        for(ExternalAccountsEntity externalAccountsEntity : externalAccountsEntities)
        {
            String externalAcctID = externalAccountsEntity.getExternalAcctID();
            int sysAcctID = externalAccountsEntity.getAccount().getAcctID();
            pairedAcctIds.put(externalAcctID, sysAcctID);
        }
        return pairedAcctIds;
    }

    public List<Integer> getSystemAccountIdsForUser(int userID)
    {
        List<AccountEntity> accountEntities = accountService.getListOfAccountsByUserID(userID);
        return accountEntities.stream()
                .map(AccountEntity::getAcctID)
                .collect(Collectors.toList());
    }

    public Boolean checkSinglePlaidAccountIsLinked(final PlaidAccount plaidAccount)
    {
        if(plaidAccount == null)
        {
            return false;
        }

        // Is the Plaid account acctID linked to a system acctID in the external Accounts table?
        // Get a map that has the key/value pairs with the plaid acctID as key and the sys acctID as the value
        // Get the externalAcctID
        String plaidAcctID = plaidAccount.getAccountId();

        // Is there a record in the ExternalAccounts table?
        Optional<ExternalAccountsEntity> externalAccountsEntity = externalAccountsService.getExternalAccount(plaidAcctID);
        if(externalAccountsEntity.isPresent())
        {
            ExternalAccountsEntity externalAccounts = externalAccountsEntity.get();
            String externalAcctID = externalAccounts.getExternalAcctID();
            Map<String, Integer> acctIDPair = getSingleSysAndPlaidAcctIdMap(externalAccounts);
            if(externalAcctID.equals(plaidAcctID))
            {
                AccountEntity sysAccount = externalAccounts.getAccount();
                if(sysAccount != null)
                {
                    int sysAcctID = sysAccount.getAcctID();
                    UserEntity user = sysAccount.getUser();
                    if(user != null)
                    {
                        List<Integer> systemAccountIds = getSystemAccountIdsForUser(user.getUserID());
                        if(systemAccountIds.contains(sysAcctID))
                        {
                            if(acctIDPair.containsValue(sysAcctID)
                                    && acctIDPair.containsKey(plaidAcctID))
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the Plaid accounts are linked to corresponding system accounts.
     *
     * @param externalAccountsEntities The list of external accounts entities.
     * @param plaidAccounts The list of unlinked Plaid accounts.
     * @return True if all the unlinked Plaid accounts are linked to their corresponding system accounts, false otherwise.
     * @throws IllegalArgumentException if externalAccountsEntities or unlinkedPlaidAccounts is null.
     */
    public Boolean checkPlaidAccountsAreLinked(final List<ExternalAccountsEntity> externalAccountsEntities, final List<PlaidAccount> plaidAccounts)
    {
        if(externalAccountsEntities == null || plaidAccounts == null)
        {
            return false;
        }
        Map<String, Integer> pairedAcctIds = getSysAndExternalAcctIDs(externalAccountsEntities);
        if(externalAccountsEntities.isEmpty())
        {
            return false;
        }
        AccountEntity account = externalAccountsEntities.get(0).getAccount();
        UserEntity user = account.getUser();
        int userID = user.getUserID();

        List<Integer> userSysAcctIDs = getSystemAccountIdsForUser(userID);
        for(PlaidAccount plaidAccount : plaidAccounts)
        {
            String plaidAcctID = plaidAccount.getAccountId();
            if(!pairedAcctIds.containsKey(plaidAcctID))
            {
                return false;
            }
            else
            {
                Integer sysAcctID = pairedAcctIds.get(plaidAcctID);
                if(!userSysAcctIDs.contains(sysAcctID))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private List<ExternalAccountsEntity> getExternalAccountsList() {
        return externalAccountsService.findAll();
    }


    private List<AccountEntity> getAccountListByUser(UserEntity user) {
        return user.getAccounts()
                .stream().toList();
    }

}


