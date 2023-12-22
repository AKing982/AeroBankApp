package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BalanceHistory;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface BalanceHistoryDAO extends ServiceDAOModel<BalanceHistory>
{
    @Override
    List<BalanceHistory> findAll();

    @Override
    void save(BalanceHistory obj);

    @Override
    void delete(BalanceHistory obj);

    @Override
    BalanceHistory findAllById(Long id);

    @Override
    List<BalanceHistory> findByUserName(String user);
}
