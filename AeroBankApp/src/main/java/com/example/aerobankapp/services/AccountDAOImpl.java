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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class AccountDAOImpl implements AccountDAO
{
    @PersistenceContext
    private final EntityManager entityManager;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountDAOImpl(EntityManager entityManager, AccountRepository accountRepository)
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
        TypedQuery<AccountEntity> accountEntityTypedQuery = getEntityManager().createQuery("SELECT e.username FROM UserEntity e WHERE e.username=:user", AccountEntity.class);
        accountEntityTypedQuery.setParameter("user", user);
        accountEntityTypedQuery.setMaxResults(1);
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
}
