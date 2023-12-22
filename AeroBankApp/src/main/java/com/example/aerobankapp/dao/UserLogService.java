package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface UserLogService extends ServiceDAOModel<UserLogEntity>
{
    @Override
    void save(UserLogEntity obj);

    @Override
    void delete(UserLogEntity obj);

    @Override
    List<UserLogEntity> findAll();

    @Override
    UserLogEntity findAllById(Long id);
}
