package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserLogService extends ServiceDAOModel<UserLogEntity>
{
    @Override
    List<UserLogEntity> findAll();

    @Override
    void save(UserLogEntity obj);

    @Override
    void delete(UserLogEntity obj);

    @Override
    Optional<UserLogEntity> findAllById(Long id);

    @Override
    List<UserLogEntity> findByUserName(String user);
    Optional<UserLogEntity> findUserLogEntriesByActiveStateAndUserID(boolean isActive, int userID);
    List<UserLogEntity> getUserLogsByLastLogin(Long id, LocalDateTime lastLogin);
    UserLogEntity getUserLogByNumberOfLoginAttempts(int attempts, int userID);
    void updateLastLogout(Long id, LocalDateTime time);
    void updateLastLogin(Long id, LocalDateTime time);
    void updateSessionDuration(Long id, int duration);
    void updateIsActiveState(Long id, boolean isActive);
    void updateLoginAttempts(Long id, int attempts);
    void updateUser(Long id, UserEntity userEntity);
    int getCurrentLoggedOnUserID(Long id);
    boolean isUserCurrentlyLoggedIn(int userID);


}
