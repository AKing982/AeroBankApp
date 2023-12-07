package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface CheckingRepositoryService extends ServiceDAOModel<CheckingAccountEntity>
{
    @Override
    List<CheckingAccountEntity> findAll();

    @Override
    void save(CheckingAccountEntity obj);

    @Override
    void delete(CheckingAccountEntity obj);

    @Override
    CheckingAccountEntity findAllById(Long id);
}
