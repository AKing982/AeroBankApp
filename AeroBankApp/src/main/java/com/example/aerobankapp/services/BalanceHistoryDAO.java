package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface BalanceHistoryDAO extends ServiceDAOModel<BalanceHistoryEntity>
{
    @Override
    List<BalanceHistoryEntity> findAll();

    @Override
    void save(BalanceHistoryEntity obj);

    @Override
    void delete(BalanceHistoryEntity obj);

    @Override
    BalanceHistoryEntity findAllById(Long id);

    @Override
    List<BalanceHistoryEntity> findByUserName(String user);
}
