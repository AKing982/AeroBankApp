package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.SavingsAccount;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface SavingsAccountService extends ServiceDAOModel<SavingsAccount>
{
    @Override
    List<SavingsAccount> findAll();

    @Override
    void save(SavingsAccount obj);

    @Override
    void delete(SavingsAccount obj);

    @Override
    SavingsAccount findAllById(Long id);
}
