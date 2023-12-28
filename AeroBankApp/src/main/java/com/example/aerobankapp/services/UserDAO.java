package com.example.aerobankapp.services;
;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface UserDAO extends ServiceDAOModel<UserEntity >
{
    @Override
    List<UserEntity> findAll();

    @Override
    void save(UserEntity obj);

    @Override
    void delete(UserEntity obj);

    @Override
    UserEntity findAllById(Long id);

    @Override
    List<UserEntity> findByUserName(String user);




}
