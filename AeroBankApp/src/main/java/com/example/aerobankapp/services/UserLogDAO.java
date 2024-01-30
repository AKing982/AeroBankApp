package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;


public interface UserLogDAO extends ServiceDAOModel<UserLogEntity>
{
    @Override
    List<UserLogEntity> findAll();

    @Override
    void save(UserLogEntity obj);

    @Override
    void delete(UserLogEntity obj);

    @Override
    Optional<UserLogEntity> findAllById(Long id);
}