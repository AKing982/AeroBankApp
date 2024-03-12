package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountSecurityService extends ServiceDAOModel<AccountSecurityEntity>
{
    @Override
    List<AccountSecurityEntity> findAll();

    @Override
    void save(AccountSecurityEntity obj);

    @Override
    void delete(AccountSecurityEntity obj);

    @Override
    Optional<AccountSecurityEntity> findAllById(Long id);

    @Override
    List<AccountSecurityEntity> findByUserName(String user);

    BigDecimal getMinimumBalanceRequirementsByAcctID(int acctID);

}
