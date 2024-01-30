package com.example.aerobankapp.services;
;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public interface UserService extends ServiceDAOModel<UserEntity>
{
    @Override
    List<UserEntity> findAll();

    @Override
    void save(UserEntity obj);

    @Override
    void delete(UserEntity obj);

    @Override
    Optional<UserEntity> findAllById(Long id);

    @Override
    List<UserEntity> findByUserName(String user);

    Role getUserRole(String user) throws NoResultException;

    UserEntity registerUser(RegistrationDTO registrationDTO);

    String getAccountNumber(String user);
}