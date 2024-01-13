package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.InvestmentAccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface InvestmentAccountDAO extends ServiceDAOModel<InvestmentAccountEntity>
{
    @Override
    List<InvestmentAccountEntity> findAll();

    @Override
    void save(InvestmentAccountEntity obj);

    @Override
    void delete(InvestmentAccountEntity obj);

    @Override
    InvestmentAccountEntity findAllById(Long id);

    @Override
    List<InvestmentAccountEntity> findByUserName(String user);
}
