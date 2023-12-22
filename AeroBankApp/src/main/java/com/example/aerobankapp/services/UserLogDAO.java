package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.UserLogModel;

import java.util.List;


public interface UserLogDAO extends ServiceDAOModel<UserLogEntity>
{
    @Override
    List<UserLogEntity> findAll();

    @Override
    void save(UserLogEntity obj);

    @Override
    void delete(UserLogEntity obj);

    @Override
    UserLogEntity findAllById(Long id);
}
