package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.PlaidTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        return List.of();
    }

    @Override
    public List<PlaidTransactionEntity> getPendingTransactionsByUser(UserEntity user) {
        return List.of();
    }

    @Override
    public Optional<PlaidTransactionEntity> getTransactionByExternalAcctId(String externalAcctId) {
        return Optional.empty();
    }

    @Override
    public Optional<PlaidTransactionEntity> getTransactionsByAmountBetweenAndUser(BigDecimal minAmount, BigDecimal maxAmount, UserEntity user) {
        return Optional.empty();
    }

    @Override
    public List<PlaidTransactionEntity> getTransactionsByDateRangeAndUser(LocalDate startDate, LocalDate endDate, UserEntity user) {
        return List.of();
    }
}
