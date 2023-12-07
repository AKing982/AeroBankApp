package com.example.aerobankapp.services;



import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface SavingsAccountService extends ServiceDAOModel<SavingsAccountEntity>
{
    @Override
    List<SavingsAccountEntity> findAll();

    @Override
    void save(SavingsAccountEntity obj);

    @Override
    void delete(SavingsAccountEntity obj);

    @Override
    SavingsAccountEntity findAllById(Long id);
}
