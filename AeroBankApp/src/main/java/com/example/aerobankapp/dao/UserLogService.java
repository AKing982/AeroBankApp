package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.ServiceModel;

import java.util.List;

public interface UserLogService extends ServiceModel<UserLog>
{
    @Override
    void save(UserLog obj);

    @Override
    void delete(UserLog obj);

    @Override
    List<UserLog> findAll();

    @Override
    UserLog findAllById(int id);
}
