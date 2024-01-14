package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends ServiceDAOModel<AccountEntity>
{
    @Override
    List<AccountEntity> findAll();

    @Override
    void save(AccountEntity obj);

    @Override
    void delete(AccountEntity obj);

    @Override
    Optional<AccountEntity> findAllById(Long id);

    @Override
    List<AccountEntity> findByUserName(String user);
}
