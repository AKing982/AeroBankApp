package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.ServiceModel;

import java.util.List;

public interface UserLogService extends ServiceModel<UserLog>
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
