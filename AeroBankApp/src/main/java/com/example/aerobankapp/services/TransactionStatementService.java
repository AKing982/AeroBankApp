package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionStatementEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface TransactionStatementService extends ServiceDAOModel<TransactionStatementEntity>
{
    @Override
    List<TransactionStatementEntity> findAll();

    @Override
    void save(TransactionStatementEntity obj);

    @Override
    void delete(TransactionStatementEntity obj);

    @Override
    Optional<TransactionStatementEntity> findAllById(Long id);

    @Override
    List<TransactionStatementEntity> findByUserName(String user);

    List<TransactionStatementEntity> getTransactionStatementsByAcctID(int acctID);




}
