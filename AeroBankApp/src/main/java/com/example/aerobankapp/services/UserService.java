package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.User;
import com.example.aerobankapp.model.ServiceModel;

import java.util.List;

public interface UserService extends ServiceModel<User>
{
    @Override
    List<User> findAll();

    @Override
    void save(User obj);

    @Override
    void delete(User obj);

    @Override
    User findAllById(int id);
}