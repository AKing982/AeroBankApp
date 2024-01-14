package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface BalanceHistoryDAO extends ServiceDAOModel<BalanceHistoryEntity>
{
    @Override
    List<BalanceHistoryEntity> findAll();

    @Override
    void save(BalanceHistoryEntity obj);

    @Override
    void delete(BalanceHistoryEntity obj);

    @Override
    Optional<BalanceHistoryEntity> findAllById(Long id);

    @Override
    List<BalanceHistoryEntity> findByUserName(String user);
}
