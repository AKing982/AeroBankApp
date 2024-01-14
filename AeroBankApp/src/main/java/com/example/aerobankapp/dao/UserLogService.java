package com.example.aerobankapp.dao;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface UserLogService extends ServiceDAOModel<UserLogEntity>
{
    @Override
    void save(UserLogEntity obj);

    @Override
    void delete(UserLogEntity obj);

    @Override
    List<UserLogEntity> findAll();

    @Override
    Optional<UserLogEntity> findAllById(Long id);
}
