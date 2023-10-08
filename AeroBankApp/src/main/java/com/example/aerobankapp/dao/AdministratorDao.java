package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.Administrator;

import java.util.List;

public interface AdministratorDao extends DaoModel<Administrator>
{
    @Override
    void save(Administrator obj);

    @Override
    void update(Administrator obj);

    @Override
    void delete(Administrator obj);

    @Override
    List<Administrator> findAll();

    @Override
    Administrator findById(int id);
}
