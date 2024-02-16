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
       // TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT e FROM AccountEntity e INNER JOIN e.userID WHERE e.username=:user", AccountEntity.class);
        TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT a FROM AccountEntity a JOIN a.users u WHERE u.username =: username", AccountEntity.class);
        accountEntityTypedQuery.setParameter("username", user);
        accountEntityTypedQuery.setMaxResults(10);
        return accountEntityTypedQuery.getResultList();
    }

    @Override
    public BigDecimal getBalanceByAcctID(String acctID)
    {
       TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT e.balance FROM AccountEntity e WHERE e.acctID=:acctID", AccountEntity.class);
       accountEntityTypedQuery.setParameter("acctID", acctID);
       accountEntityTypedQuery.setMaxResults(1);
       List<AccountEntity> results = accountEntityTypedQuery.getResultList();
       if(!results.isEmpty())
       {
           return results.get(0).getBalance();
       }
       else
       {
           return null;
       }
    }

    @Override
    public BigDecimal getBalanceByAccountCodeUserID(String acctCode, int userID) {
        TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT e FROM AccountEntity e WHERE e.accountCode=:acctCode AND e.userID=:userID", AccountEntity.class);
        accountEntityTypedQuery.setParameter("acctCode", acctCode);
        accountEntityTypedQuery.setParameter("userID", userID);
        accountEntityTypedQuery.setMaxResults(1);
        List<AccountEntity> results = accountEntityTypedQuery.getResultList();

        BigDecimal balance = results.get(0).getBalance();
        if(balance == null)
        {
            throw new IllegalArgumentException("Invalid Balance Found.");
        }
        else
        {
            return balance;
        }
    }

    @Override
    public BigDecimal getTotalAccountBalances(String user)
    {
        String totalBalancesQuery =  "SELECT SUM(a.balance) as TotalBalance " +
                "FROM AccountEntity a " +
                "JOIN a.users u " + // Assuming 'user' is the field name in AccountEntity for the UserEntity relationship
                "WHERE u.username = :username";;

        TypedQuery<BigDecimal> accountEntityTypedQuery = getEntityManager().createQuery(totalBalancesQuery, BigDecimal.class);
        accountEntityTypedQuery.setParameter("username", user);
        return accountEntityTypedQuery.getSingleResult();
    }

    @Override
    public Long getNumberOfAccounts(String user)
    {
        String totalAccountsQuery =  "SELECT COUNT(a) " +
                "FROM AccountEntity a " +
                "JOIN a.users u " + // Assuming 'user' is the field name in AccountEntity that refers to UserEntity
                "WHERE u.username = :username";

        TypedQuery<Long> totalAccounts = getEntityManager().createQuery(totalAccountsQuery, Long.class);
        totalAccounts.setParameter("username", user);
        return totalAccounts.getSingleResult();

    }

    @Override
    public List<String> getListOfAccountCodes(String user) {
        String accountCodesQuery = "SELECT a.accountCode " +
                    "FROM AccountEntity a " +
                    "JOIN a.users u " +
                    "WHERE u.username = :username";
        TypedQuery<String> accountCodes = getEntityManager().createQuery(accountCodesQuery, String.class);
        accountCodes.setParameter("username", user);
        return accountCodes.getResultList();
    }

    @Override
    public int getAccountIDByAcctCodeAndUserID(int userID, String acctCode) {
        if (!doesAccountCodeExist(acctCode)) {
            throw new IllegalArgumentException("Account Code does not exist.");
        }
        if (isInvalidUserID(userID)) {
            throw new IllegalArgumentException("Invalid User ID");
        }
        final String accountIDQuery = "SELECT a.acctID " +
                "FROM AccountEntity a " +
                "WHERE a.userID =:userID AND a.accountCode =:acctCode";
        TypedQuery<Integer> accountID = getEntityManager().createQuery(accountIDQuery, Integer.class);
        accountID.setParameter("userID", userID);
        accountID.setParameter("acctCode", acctCode);
        Integer result = accountID.getSingleResult();
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
        if(userName.isEmpty())
        {
            throw new IllegalArgumentException("Invalid UserName has been entered");
        }
        final String accountTypeToAccountIDQuery = "SELECT a FROM AccountEntity a JOIN a.users u WHERE u.username =: userName";
        TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery(accountTypeToAccountIDQuery, AccountEntity.class);
        accountEntityTypedQuery.setParameter("userName", userName);
        List<AccountEntity> accountEntities = accountEntityTypedQuery.getResultList();

        Map<Integer, String> accountTypeMap = new HashMap<>();
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
        final String updateBalanceStatement = "UPDATE AccountEntity a SET a.balance =: balance WHERE a.acctID=: acctID";
        Query accountEntityQuery = getEntityManager().createQuery(updateBalanceStatement);
        accountEntityQuery.setParameter("balance", balance);
        accountEntityQuery.setParameter("acctID", acctID);

        int updateCount = accountEntityQuery.executeUpdate();
    }

    private boolean doesAccountCodeExist(String acctCode)
    {
        final String query = "SELECT COUNT(a) FROM AccountEntity a WHERE a.accountCode =: acctCode";
        TypedQuery<Long> acctCodeQuery = getEntityManager().createQuery(query, Long.class);
        acctCodeQuery.setParameter("acctCode", acctCode);
        return acctCodeQuery.getSingleResult() > 0;
    }

    private boolean doesAccountIDExist(int acctID)
    {
        final String query = "SELECT COUNT(a) FROM AccountEntity a WHERE a.acctID =: acctID";
        TypedQuery<Integer> acctCodeQuery = getEntityManager().createQuery(query, Integer.class);
        acctCodeQuery.setParameter("acctID", acctID);
        return acctCodeQuery.getSingleResult() > 0;
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
