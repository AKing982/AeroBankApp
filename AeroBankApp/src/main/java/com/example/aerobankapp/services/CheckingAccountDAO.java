package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface CheckingAccountDAO extends ServiceDAOModel<CheckingAccountEntity>
{
    @Override
    List<CheckingAccountEntity> findAll();

    @Override
    void save(CheckingAccountEntity obj);

    @Override
    void delete(CheckingAccountEntity obj);

    @Override
    Optional<CheckingAccountEntity> findAllById(Long id);
}
