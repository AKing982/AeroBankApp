package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface UserLogService extends ServiceDAOModel<UserLog>
{
    @Override
    List<UserLog> findAll();

    @Override
    void save(UserLog obj);

    @Override
    void delete(UserLog obj);

    @Override
    UserLog findAllById(int id);
}
