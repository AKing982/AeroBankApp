package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ExternalTransactionEntity;
import com.example.aerobankapp.repositories.ExternalTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalTransactionServiceImpl implements ExternalTransactionService
{
    private final ExternalTransactionsRepository externalTransactionsRepository;

    @Autowired
    public ExternalTransactionServiceImpl(ExternalTransactionsRepository externalTransactionsRepository)
    {
        this.externalTransactionsRepository = externalTransactionsRepository;
    }

    @Override
    public List<ExternalTransactionEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(ExternalTransactionEntity obj) {
        externalTransactionsRepository.save(obj);
    }

    @Override
    public void delete(ExternalTransactionEntity obj) {

    }

    @Override
    public Optional<ExternalTransactionEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ExternalTransactionEntity> findByUserName(String user) {
        return List.of();
    }

    @Override
    public Optional<ExternalTransactionEntity> findByExternalId(String externalId) {
        return Optional.empty();
    }

    @Override
    public Optional<ExternalTransactionEntity> findByTransactionId(String transactionId) {
        return Optional.empty();
    }

    @Override
    public Optional<ExternalTransactionEntity> findBySystemAcctID(int systemAcctID) {
        return Optional.empty();
    }

    @Override
    public Boolean updateExternalTransaction(ExternalTransactionEntity externalTransactionEntity) {
        return null;
    }
}
