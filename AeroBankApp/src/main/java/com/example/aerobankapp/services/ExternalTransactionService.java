package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ExternalTransactionEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.Optional;

public interface ExternalTransactionService extends ServiceDAOModel<ExternalTransactionEntity>
{
    Optional<ExternalTransactionEntity> findByExternalId(String externalId);
    Optional<ExternalTransactionEntity> findByTransactionId(String transactionId);
    Optional<ExternalTransactionEntity> findBySystemAcctID(int systemAcctID);

    Boolean updateExternalTransaction(ExternalTransactionEntity externalTransactionEntity);
}
