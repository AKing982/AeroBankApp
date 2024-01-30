package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
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
        getAccountRepository().save(obj);
    }

    @Override
    public void delete(AccountEntity obj) {
        getAccountRepository().delete(obj);
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
    public BigDecimal getBalanceByAccountCode(String acctCode) {
        TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT e.balance FROM AccountEntity e WHERE e.accountCode=:acctCode", AccountEntity.class);
        accountEntityTypedQuery.setParameter("acctCode", acctCode);
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
}