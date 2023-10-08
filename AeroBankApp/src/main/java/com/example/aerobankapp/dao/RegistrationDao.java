package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.Registration;

import java.util.List;

public interface RegistrationDao extends DaoModel<Registration>
{
    @Override
    void save(Registration obj);

    @Override
    void update(Registration obj);

    @Override
    void delete(Registration obj);

    @Override
    List<Registration> findAll();

    @Override
    Registration findById(int id);
}
