package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaidTransactionService extends ServiceDAOModel<PlaidTransactionEntity>
{
    List<PlaidTransactionEntity> getTransactionsByUser(UserEntity user);

    List<PlaidTransactionEntity> getTransactionsByAccount(AccountEntity account);

    List<PlaidTransactionEntity> getPendingTransactionsByUser(UserEntity user);
    Optional<PlaidTransactionEntity> getTransactionByExternalAcctId(String externalAcctId);

    List<PlaidTransactionEntity> getTransactionsByAmountBetweenAndUser(BigDecimal minAmount, BigDecimal maxAmount, UserEntity user);

    List<PlaidTransactionEntity> getTransactionsByDateRangeAndUser(LocalDate startDate, LocalDate endDate, UserEntity user);


}
