package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface TransactionService extends ServiceDAOModel<TransactionEntity>
{
    @Override
    List<TransactionEntity> findAll();

    @Override
    void save(TransactionEntity obj);

    @Override
    void delete(TransactionEntity obj);

    @Override
    Optional<TransactionEntity> findAllById(Long id);

    @Override
    List<TransactionEntity> findByUserName(String user);
}
