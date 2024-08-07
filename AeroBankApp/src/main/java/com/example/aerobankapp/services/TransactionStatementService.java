package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransactionStatementEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionStatementService extends ServiceDAOModel<TransactionStatementEntity>
{
    @Override
    List<TransactionStatementEntity> findAll();

    TransactionStatementEntity createTransactionStatementEntity(AccountEntity account, String description, BigDecimal debit, BigDecimal credit, BigDecimal balance);

    @Override
    void save(TransactionStatementEntity obj);

    @Override
    void delete(TransactionStatementEntity obj);

    @Override
    Optional<TransactionStatementEntity> findAllById(Long id);

    @Override
    List<TransactionStatementEntity> findByUserName(String user);

    List<TransactionStatementEntity> getTransactionStatementsByAcctID(int acctID);

    List<TransactionStatementEntity> getPendingTransactionsByAcctID(int acctID);

    int getCountOfPendingTransactionsByAcctID(int acctID);



}
