package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface AccountCodeService extends ServiceDAOModel<AccountCodeEntity>
{
    @Override
    List<AccountCodeEntity> findAll();

    @Override
    void save(AccountCodeEntity obj);

    @Override
    void delete(AccountCodeEntity obj);

    @Override
    Optional<AccountCodeEntity> findAllById(Long id);

    @Override
    List<AccountCodeEntity> findByUserName(String user);

    AccountCodeEntity buildAccountCodeEntity(AccountCodeDTO accountCodeDTO);
}
