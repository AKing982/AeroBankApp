package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.exceptions.AccountIDNotFoundException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.ZeroBalanceException;
import com.example.aerobankapp.repositories.AccountRepository;
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

@Service
@Getter
@Setter
public class AccountServiceImpl implements AccountService
{
    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountRepository accountRepository;
    private Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    public AccountServiceImpl(EntityManager entityManager, AccountRepository accountRepository)
    {
        this.entityManager = entityManager;
        this.accountRepository = accountRepository;
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
    public void delete(AccountEntity obj)
    {
        if(obj != null)
        {
            getAccountRepository().delete(obj);
        }
    }

    @Override
    public Optional<AccountEntity> findAllById(Long id) {
        return getAccountRepository().findById(id);
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

    @Override
    public BigDecimal getTotalAccountBalances(String user)
    {
        BigDecimal totalBalances = accountRepository.getTotalAccountBalances(user);
        if(totalBalances == null)
        {
            throw new IllegalArgumentException("Found No Balances for this account.");
        }
        return totalBalances;
    }

    @Override
    public Long getNumberOfAccounts(String user)
    {
        return accountRepository.getNumberOfAccounts(user);
    }

    @Override
    public int getAccountIDByAccountCodeAndAccountNumber(String acctCode, String accountNumber) {
        if(acctCode.isEmpty() || accountNumber.isEmpty()){
            throw new IllegalArgumentException("Caught empty AccountCode or AccountNumber.");
        }
        return accountRepository.findAccountIDByAcctCodeAndAccountNumber(accountNumber, acctCode);
    }

    @Override
    public List<String> getListOfAccountCodes(String user) {
        List<String> accountCodesList = accountRepository.findAccountCodesByUserName(user);
        return accountRepository.findAccountCodesByUserName(user);
    }

    @Override
    public List<String> getAccountCodeListByAccountNumber(String accountNumber) {
        return accountRepository.findAccountCodesByAccountNumber(accountNumber);
    }

    @Override
    public int getAccountIDByAcctCodeAndUserID(int userID, String acctCode) {
        if (!doesAccountCodeExist(acctCode)) {
            throw new IllegalArgumentException("Account Code does not exist.");
        }
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

    public boolean isInvalidUserID(int userID)
    {
        return userID <= 0;
    }

    public boolean isValidAcctID(int acctID)
    {
        return acctID > 0 && doesAccountIDExist(acctID);
    }
}
