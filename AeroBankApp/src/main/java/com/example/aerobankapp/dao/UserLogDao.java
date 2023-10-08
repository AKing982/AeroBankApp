package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.UserLog;

import java.util.List;

public interface UserLogDao extends DaoModel<UserLog>
{
    @Override
    void save(UserLog obj);

    @Override
    void update(UserLog obj);

    @Override
    void delete(UserLog obj);

    @Override
    List<UserLog> findAll();

    @Override
    UserLog findById(int id);
}
