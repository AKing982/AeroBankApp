package com.example.aerobankapp.services;

import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.UserLogModel;

import java.util.List;

public interface UserLogService extends ServiceDAOModel<UserLogModel>
{
    @Override
    List<UserLogModel> findAll();

    @Override
    void save(UserLogModel obj);

    @Override
    void delete(UserLogModel obj);

    @Override
    UserLogModel findAllById(int id);
}
