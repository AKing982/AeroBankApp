package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.PendingTransactions;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface PendingTransactionsService extends ServiceDAOModel<PendingTransactions>
{
    @Override
    List<PendingTransactions> findAll();

    @Override
    void save(PendingTransactions obj);

    @Override
    void delete(PendingTransactions obj);

    @Override
    PendingTransactions findAllById(Long id);

    @Override
    List<PendingTransactions> findByUserName(String user);
}
