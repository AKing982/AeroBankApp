package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.TransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidAccountIDException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.example.aerobankapp.model.TransactionCriteria;
import com.example.aerobankapp.repositories.PlaidTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidTransactionServiceImpl implements PlaidTransactionService
{
    private final PlaidTransactionRepository plaidTransactionRepository;

    @Autowired
    public PlaidTransactionServiceImpl(PlaidTransactionRepository plaidTransactionRepository)
    {
        this.plaidTransactionRepository = plaidTransactionRepository;
    }

    @Override
    public PlaidTransactionEntity createPlaidTransactionEntity(UserEntity userEntity, AccountEntity accountEntity, PlaidTransactionCriteria transactionCriteria)
    {
        PlaidTransactionEntity plaidTransactionEntity = new PlaidTransactionEntity();
        plaidTransactionEntity.setAccount(accountEntity);
        plaidTransactionEntity.setUser(userEntity);
        plaidTransactionEntity.setMerchantName(transactionCriteria.getMerchantName());
        plaidTransactionEntity.setCreatedAt(LocalDateTime.now());
        plaidTransactionEntity.setAuthorizedDate(transactionCriteria.getAuthorizedDate());
        plaidTransactionEntity.setDate(transactionCriteria.getDate());
        plaidTransactionEntity.setExternalId(transactionCriteria.getExternalId());
        plaidTransactionEntity.setPending(transactionCriteria.isPending());
        plaidTransactionEntity.setName(transactionCriteria.getDescription());
        plaidTransactionEntity.setAmount(transactionCriteria.getAmount());
        plaidTransactionEntity.setExternalAcctID(transactionCriteria.getExternalAcctID());
        return plaidTransactionEntity;
    }

    @Override
    public List<PlaidTransactionEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(PlaidTransactionEntity obj) {
        if(obj == null)
        {
            throw new IllegalArgumentException("Invalid PlaidTransactionEntity");
        }
       // assertPlaidTransactionParametersAreNull(obj);

        plaidTransactionRepository.save(obj);
    }

    private void assertPlaidTransactionParametersAreNull(PlaidTransactionEntity obj)
    {
        Assert.isNull(obj.getAccount(), "Account cannot be null");
        Assert.isNull(obj.getAmount(), "Amount cannot be null");
        Assert.isNull(obj.getId(), "Id cannot be null");
        Assert.isNull(obj.getDate(), "Date cannot be null");
        Assert.isNull(obj.getName(), "Name cannot be null");
        Assert.isNull(obj.getUser(), "User cannot be null");
        Assert.isNull(obj.getExternalAcctID(), "External Account ID cannot be null");
        Assert.isNull(obj.getExternalId(), "External ID cannot be null");
        Assert.isNull(obj.getMerchantName(), "Merchant Name cannot be null");
        Assert.isNull(obj.getAuthorizedDate(), "Authorized Date cannot be null");
    }

    @Override
    public void delete(PlaidTransactionEntity obj) {
        if(obj == null)
        {
            throw new IllegalArgumentException("Invalid PlaidTransactionEntity");
        }
        plaidTransactionRepository.delete(obj);
    }

    @Override
    public Optional<PlaidTransactionEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PlaidTransactionEntity> findByUserName(String user) {
        return List.of();
    }

    @Override
    public List<PlaidTransactionEntity> getTransactionsByUser(UserEntity user) {
        assertUserEntityNull(user);
        if(user.getUserID() < 1)
        {
            throw new InvalidUserIDException("UserID Is invalid");
        }
        List<PlaidTransactionEntity> plaidTransactionEntities = plaidTransactionRepository.findByUserId(user.getUserID());
        return plaidTransactionEntities;
    }

    private void assertUserEntityNull(UserEntity obj)
    {
        if(obj == null)
        {
            throw new IllegalArgumentException("Invalid UserEntity");
        }
    }

    @Override
    public List<PlaidTransactionEntity> getTransactionsByAccount(AccountEntity account) {
        if(account == null)
        {
            throw new IllegalArgumentException("Invalid AccountEntity");
        }

        if(account.getAcctID() < 1)
        {
            throw new InvalidAccountIDException("Account ID Is invalid");
        }
        return plaidTransactionRepository.findByAccountId(account.getAcctID());
    }

    @Override
    public List<PlaidTransactionEntity> getPendingTransactionsByUser(UserEntity user) {
        if(user == null)
        {
            throw new IllegalArgumentException("User is null");
        }
        return plaidTransactionRepository.findByUserId(user.getUserID());
    }

    @Override
    public Optional<PlaidTransactionEntity> getTransactionByExternalAcctId(String externalAcctId) {
        if(externalAcctId == null || externalAcctId.isEmpty())
        {
            throw new IllegalArgumentException("External Account ID cannot be null or empty");
        }
        return plaidTransactionRepository.findByExternalAcctID(externalAcctId);
    }

    @Override
    public List<PlaidTransactionEntity> getTransactionsByAmountBetweenAndUser(BigDecimal minAmount, BigDecimal maxAmount, UserEntity user) {
        if(minAmount == null || maxAmount == null)
        {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return plaidTransactionRepository.findByAmountBetweenAndUserId(minAmount, maxAmount, user.getUserID());
    }

    @Override
    public List<PlaidTransactionEntity> getTransactionsByDateRangeAndUser(LocalDate startDate, LocalDate endDate, UserEntity user) {
        if(startDate == null || endDate == null || user == null)
        {
            throw new IllegalArgumentException("Transaction criteria is null");
        }
        return plaidTransactionRepository.findByDateBetweenAndUserId(startDate, endDate, user.getUserID());
    }
}
