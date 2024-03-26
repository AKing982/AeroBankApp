package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.PendingTransactionEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface PendingTransactionsService extends ServiceDAOModel<PendingTransactionEntity>
{
    @Override
    void save(PendingTransactionEntity obj);

    @Override
    void delete(PendingTransactionEntity obj);

    @Override
    List<PendingTransactionEntity> findAll();

    @Override
    Optional<PendingTransactionEntity> findAllById(Long id);

    @Override
    List<PendingTransactionEntity> findByUserName(String user);
}
