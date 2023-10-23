package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface CheckingRepositoryService extends ServiceDAOModel<CheckingAccount>
{
    @Override
    List<CheckingAccount> findAll();

    @Override
    void save(CheckingAccount obj);

    @Override
    void delete(CheckingAccount obj);

    @Override
    CheckingAccount findAllById(int id);
}
