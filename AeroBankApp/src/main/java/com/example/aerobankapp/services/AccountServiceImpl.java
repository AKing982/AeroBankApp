package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.exceptions.AccountIDNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
@Setter
@Transactional
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
    public BigDecimal getBalanceByAcctID(int acctID)
    {
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
    public List<String> getListOfAccountCodes(String user) {
        List<String> accountCodesList = accountRepository.findAccountCodesByUserName(user);
        if(accountCodesList.isEmpty())
        {

        }
        return accountRepository.findAccountCodesByUserName(user);
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
        if(!isValidAcctID(acctID))
        {
            throw new IllegalArgumentException("Invalid Account ID Found");
        }
        if(balance == null)
        {
            throw new IllegalArgumentException("Invalid Balance has been entered.");
        }

        accountRepository.updateAccountBalanceByAcctID(balance, acctID);
    }

    private boolean doesAccountCodeExist(String acctCode)
    {
        return accountRepository.doesAccountCodeExist(acctCode);
    }

    private boolean doesAccountIDExist(int acctID)
    {
       return accountRepository.doesAccountIDExist(acctID);
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
