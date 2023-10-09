package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Administrator;
import com.example.aerobankapp.model.ServiceModel;

import java.util.List;

public interface AdministratorService extends ServiceModel<Administrator>
{
    @Override
    List<Administrator> findAll();

    @Override
    Administrator findAllById(int id);

    @Override
    void save(Administrator obj);

    @Override
    void delete(Administrator obj);

}
