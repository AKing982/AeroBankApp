package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Administrator;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface AdministratorService extends ServiceDAOModel<Administrator>
{
    @Override
    List<Administrator> findAll();

    @Override
    Administrator findAllById(Long id);

    @Override
    void save(Administrator obj);

    @Override
    void delete(Administrator obj);

}
