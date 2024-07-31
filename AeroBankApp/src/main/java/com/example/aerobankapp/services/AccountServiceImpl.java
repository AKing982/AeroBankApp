package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.repositories.AccountPropertiesRepository;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.example.aerobankapp.services.utilities.AccountSecurityServiceUtil.buildSecurityEntity;

@Service
@Getter
@Setter
public class AccountServiceImpl implements AccountService
{
    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountRepository accountRepository;
    private final AccountSecurityRepository accountSecurityRepository;
    private final AccountPropertiesRepository accountPropertiesRepository;
    private Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    public AccountServiceImpl(EntityManager entityManager, AccountRepository accountRepository, AccountSecurityRepository accountSecurityRepository, AccountPropertiesRepository accountPropertiesRepository)
    {
        this.entityManager = entityManager;
        this.accountRepository = accountRepository;
        this.accountSecurityRepository = accountSecurityRepository;
        this.accountPropertiesRepository = accountPropertiesRepository;
    }

    @Override
    public List<AccountEntity> findAll() {
        return getAccountRepository().findAll();
    }

    @Override
    public void save(AccountEntity obj) {
        if(obj != null)
        {
            getAccountRepository().save(obj);
        }
    }

    @Override
    @Transactional
    public void delete(AccountEntity obj)
    {
        if(obj != null)
        {
            getAccountRepository().delete(obj);
        }
    }

    @Override
    @Transactional
    public void deleteAll(List<AccountEntity> accountEntities) {
        accountRepository.deleteAll(accountEntities);
    }

    @Override
    public Optional<AccountEntity> findAllById(Long id) {
        return getAccountRepository().findById(id);
    }

    @Override
    public Optional<AccountEntity> findById(int id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<AccountEntity> findByUserId(int userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public List<AccountEntity> findByUserName(String user) {
       return accountRepository.findByUserName(user);
    }

    @Override
    @Transactional
    public BigDecimal getBalanceByAcctID(int acctID)
    {
        LOGGER.info("AcctID: " + acctID);
        if(getAccountRepository() == null){
            LOGGER.error("Null Account Repository");
        }
        BigDecimal balance = getAccountRepository().getBalanceByAcctID(acctID);
        if(balance == null){
            throw new IllegalArgumentException("Obtained Illegal Balance for AcctID: " + acctID);
        }
        return accountRepository.getBalanceByAcctID(acctID);
    }

    @Override
    public BigDecimal getBalanceByAccountCodeUserID(String acctCode, int userID) {
        if(acctCode == null || userID <= 1) {
            throw new IllegalArgumentException("Invalid Account Code or UserID.");
        }
        BigDecimal balance = accountRepository.findBalanceByAccountCodeAndUserID(acctCode, userID);
        if(balance == null) {
            throw new IllegalArgumentException("Invalid Balance Found.");
        }
        return balance;
    }

    /**
     * Retrieves the total balances for the given user.
     *
     * IMPORTANT NOTE:
     * If This method returns null for the Total Account Balances, then this might indicate that
     * The Account_Users table doesn't have any acctID, userID records for the user in question.
     *
     * @param user The username of the user
     * @return The total account balances as a BigDecimal. If there are no balances found, it returns BigDecimal.ZERO
     */
    @Override
    @Transactional
    public BigDecimal getTotalAccountBalances(String user)
    {
        LOGGER.info("Getting Total Balances for User: {}", user);
        try
        {
            BigDecimal totalBalances = accountRepository.getTotalAccountBalances(user);
            if(totalBalances == null) {
                LOGGER.error("Total Balances not found for User: {}", user);
                return BigDecimal.ZERO;
            }
            LOGGER.info("Total Balances: ${}", totalBalances);
            return totalBalances;
        }catch(Exception e)
        {
            LOGGER.error("There was an error while getting Total Balances for User: {}", user, e);
            return BigDecimal.ZERO;
        }
    }



    /**
     * Retrieves the number of accounts for a given user.
     *
     * IMPORTANT NOTE:
     * If This method returns null for the Number of Accounts, then this might indicate that
     * The Account_Users table doesn't have any acctID, userID records for the user in question.
     *
     * @param user The username of the user
     * @return The number of accounts as a Long. If there are no accounts found, it returns 0L.
     */
    @Override
    @Transactional
    public Long getNumberOfAccounts(String user)
    {
        try
        {
            Long numberOfAccounts = accountRepository.getNumberOfAccounts(user);
            if(numberOfAccounts == null || numberOfAccounts <= 0)
            {
                LOGGER.error("Number of Accounts not found for User: {}", user);
                return 0L;
            }
            return numberOfAccounts;
        }catch(Exception e)
        {
            LOGGER.error("There was an error while getting Number of Accounts for User: {}", user, e);
            return 0L;
        }

    }

    @Override
    public int getAccountIDByAccountCodeAndAccountNumber(Long acctCodeID, String accountNumber) {
        if(acctCodeID < 0 || accountNumber.isEmpty()){
            throw new IllegalArgumentException("Caught empty AccountCode or AccountNumber.");
        }
         return accountRepository.getAccountIDByAcctCodeAndAccountNumber(acctCodeID,accountNumber);
    }

    @Override
    public List<String> getListOfAccountCodes(String user) {
        List<String> accountCodesList = accountRepository.findAccountNameWithAcctCodeByUserName(user);
        return accountRepository.findAccountNameWithAcctCodeByUserName(user);
    }

    @Override
    public int getAccountIDByAcctCodeAndAccountNumber(String acctCode, String accountNumber) {
        return 0;
    }

    @Override
    public List<String> getAccountNamesWithAcctSegmentByAccountNumber(String accountNumber) {
        return accountRepository.findAccountNamesWithAcctSegmentByAccountNumber(accountNumber);
    }

    @Override
    public int getAccountIDByAcctCodeAndUserID(int userID, Long acctCode) {

        if (isInvalidUserID(userID)) {
            throw new IllegalArgumentException("Invalid User ID");
        }
        Integer result = accountRepository.getAccountIDByAcctCodeAndUserID(userID, acctCode);
        if (result == null)
        {
            throw new AccountIDNotFoundException("AccountID Not Found");
        }
        return result;
    }

    @Override
    public Integer getAccountWithMostTransactionsByUserID(final int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID caught: " + userID);
        }
        Page<Object[]> result = accountRepository.findAccountWithMostTransactionsByUserID(userID, PageRequest.of(0, 1));
        if(!result.hasContent())
        {
            return null;
        }
        Object[] topResult = result.getContent().get(0);
        return (Integer) topResult[0];
    }

    @Override
    @Transactional
    public List<AccountEntity> getListOfAccountsByUserID(int userID) {
        if(userID > 0){
            return accountRepository.findAccountsByUserID(userID);
        }
        return List.of();
    }

    @Override
    @Transactional
    public Map<Integer, String> getAccountTypeMapByAccountId(String userName)
    {
        Map<Integer, String> accountTypeMap = new HashMap<>();
        if(userName.isEmpty())
        {
            throw new IllegalArgumentException("Invalid UserName has been entered");
        }
        List<AccountEntity> accountEntities = accountRepository.findByUserName(userName);

        for(AccountEntity accountEntity : accountEntities)
        {
            accountTypeMap.put(accountEntity.getAcctID(), accountEntity.getAccountType());
        }
        return accountTypeMap;
    }

    @Override
    @Transactional
    public void updateAccountBalanceByAcctID(BigDecimal balance, int acctID)
    {
        LOGGER.info("Updating Balance for AcctID: " + acctID);
        if(isValidAcctID(acctID))
        {
            throw new IllegalArgumentException("Invalid Account ID Found");
        }
        if(balance == null)
        {
            throw new IllegalArgumentException("Invalid Balance has been entered.");
        }

        accountRepository.updateAccountBalanceByAcctID(balance, acctID);
    }

    @Override
    public int getRandomAccountIDByUserID(int userID) {
        List<Integer> accountIDList = accountRepository.getListOfAccountIDsByUserID(userID);
        Random random = new Random();
        int randomAcctID = random.nextInt(accountIDList.size());
        return accountIDList.get(randomAcctID);
    }

    @Override
    public int getAccountIDByAccountName(final String accountName) {
        return accountRepository.findAccountIDByAccountName(accountName);
    }

    @Override
    @Transactional
    public int getLatestAccountID() {
        Integer maxAcctID = accountRepository.fetchLatestAccountID();
        if(maxAcctID == null){
            return 0;
        }
         return accountRepository.fetchLatestAccountID();
    }

    @Override
    public void saveAll(List<AccountEntity> accountEntities) {
        accountRepository.saveAll(accountEntities);
    }

    @Override
    @Transactional
    public void updateAccountEntityByNameAndBalanceAndMask(String name, BigDecimal balance, String mask, int acctID) {
        try
        {
            accountRepository.updateAccountEntityByNameAndBalanceAndMask(name, balance, mask, acctID);

        }catch(Exception e)
        {
            LOGGER.error("There was an error updating the account: {}", e.getMessage());
        }
    }

    @Override
    public List<String> getAccountCodeShortSegmentByUser(String user) {
        if(user.isEmpty()){
            throw new InvalidUserStringException("Unable to retrieve short segments for user: " + user);
        }
        List<String> rawAccountCodeShortSegments = accountRepository.getAccountCodeShortSegmentByUser(user);
        if(rawAccountCodeShortSegments.isEmpty()){
            throw new NonEmptyListRequiredException("Found empty list of account short segments..");
        }

        // Filter out the zeroes in the accountCode segment
        return filterOutZeroParameter(rawAccountCodeShortSegments);
    }

    @Override
    public List<Integer> getListOfAccountCodeIDsByAcctNumber(String accountNumber) {
        return accountRepository.findAccountCodeIDListByAccountNumber(accountNumber);
    }

    private List<String> filterOutZeroParameter(List<String> unfilteredSegments){
        List<String> segments = new ArrayList<>();
        for(String segment : unfilteredSegments){
            if(!segment.isEmpty()){
                String filtered = getFilterZeroParameterString(segment);
                segments.add(filtered);
            }
        }
        return segments;
    }

    private String getFilterZeroParameterString(String segment){
        StringBuilder filteredString = new StringBuilder();
        for(int i = 0; i < segment.length(); i++){
            char c = segment.charAt(i);
            if(c == '0' && i > 0 && i < segment.length() - 1){
                char prev = segment.charAt(i - 1);
                char next = segment.charAt(i + 1);

                // is the zero between a letter or digit
                if(Character.isLetter(prev) && Character.isDigit(next)){
                    continue;
                }
            }
            filteredString.append(c);
        }
        return filteredString.toString();
    }

    private boolean doesAccountCodeExist(String acctCode)
    {
        int count = accountRepository.doesAccountCodeExist(acctCode);
        return count == 1;
    }

    private boolean doesAccountIDExist(int acctID)
    {
        int accountCount = accountRepository.doesAccountIDExist(acctID);
        return accountCount > 1;
    }

    @Transactional
    public List<AccountEntity> processAccountAndSecurity(List<AccountCodeEntity> accountCodeEntities, List<AccountInfoDTO> accountInfoDTOS, UserEntity user){
        List<AccountEntity> accountEntities = new ArrayList<>();
        for (int i = 0; i < accountInfoDTOS.size(); i++) {
            AccountInfoDTO accountInfoDTO = accountInfoDTOS.get(i);
            AccountCodeEntity accountCode = accountCodeEntities.get(i);
            AccountEntity account = buildAccountEntity(accountInfoDTO, accountCode, user);
            accountRepository.saveAndFlush(account); // Ensure account is persisted before linking security

            AccountSecurityEntity accountSecurity = buildSecurityEntity(account);
            accountSecurityRepository.save(accountSecurity); // Persist and flush security entity

            account.setAccountSecurity(accountSecurity);
            accountRepository.save(account); // Save the account again with the security link

            accountEntities.add(account);
        }

        LOGGER.info("Processed {} accounts with detailed security settings.", accountEntities.size());
        return accountEntities;
    }

    private void setupAccountSecurity(AccountEntity account) {
        AccountSecurityEntity accountSecurity = new AccountSecurityEntity();
        accountSecurity.setAccount(account);
        accountSecurity.setEnabled(true);  // Set other properties as needed
        accountSecurityRepository.save(accountSecurity);
        account.setAccountSecurity(accountSecurity);  // Link security details back to the account
        accountRepository.save(account);  // Save the account again if needed
    }

    @Override
    public AccountEntity buildAccountEntity(final AccountInfoDTO accountInfoDTO, AccountCodeEntity accountCode, UserEntity user){
        AccountEntity account = new AccountEntity();

        final BigDecimal DEFAULT_INTEREST = new BigDecimal("1.67");

        if(accountInfoDTO.accountType().equals("RENT")){
            account.setRentAccount(true);
            account.setHasMortgage(false);
            account.setHasDividend(true);
        }
        if(accountInfoDTO.accountType().equals("MORTGAGE")){
            account.setHasDividend(true);
            account.setHasMortgage(true);
            account.setRentAccount(false);
        }
        // Generate the AccountCode

        account.setRentAccount(false);
        account.setHasMortgage(false);
        account.setAccountName(accountInfoDTO.accountName());
        account.setAccountType(accountInfoDTO.accountType());
        account.setBalance(accountInfoDTO.initialBalance());
        account.setHasDividend(true);
        account.setAccountCode(accountCode);
        account.setInterest(DEFAULT_INTEREST);
        account.setUser(user);
        return account;
    }



    @Override
    public AccountEntity buildAccountEntityByAccountModel(Account account, AccountCodeEntity accountCode, UserEntity user) {
        int acctID = account.getAccountID();

        // Check to see if an account exists with the above ID
        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(acctID);

        // If No account is found, throw an exception
        if(accountEntityOptional.isEmpty()){
            throw new AccountIDNotFoundException("Account with ID: " + acctID + " not found.");
        }
        return buildAccount(account, accountCode, user);
    }


    private AccountEntity buildAccount(Account account, AccountCodeEntity accountCode, UserEntity user){

        AccountSecurityEntity aes = accountSecurityRepository.findByAcctID(account.getAccountID())
                .orElseThrow(() -> new AccountIDNotFoundException("Account with ID: " + account.getAccountID() + " not found."));

        AccountPropertiesEntity accountPropertiesEntity = accountPropertiesRepository.findByAcctID(account.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account Properties not found with acctID: " + account.getAccountID()));

        return AccountEntity.builder()
                .accountType(account.getAccountType().getCode())
                .accountCode(accountCode)
                .acctID(account.getAccountID())
                .balance(account.getBalance())
                .interest(account.getInterest())
                .accountName(account.getAccountName())
                .isRentAccount(account.isRentAccount())
                .mask(account.getMask())
                .type(account.getType())
                .subtype(account.getSubType())
                .accountSecurity(aes)
                .accountProperties(accountPropertiesEntity)
                .hasMortgage(account.isMortgageAccount())
                .user(user)
                .build();
    }

    public boolean isInvalidUserID(int userID)
    {
        return userID <= 0;
    }

    public boolean isValidAcctID(int acctID)
    {
        return acctID > 0 && doesAccountIDExist(acctID);
    }
}
