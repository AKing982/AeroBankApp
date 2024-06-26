package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.sql.Timestamp;
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

    List<UserLogEntity> getUserLogListByUserID(int userID);
    void updateUser(Long id, UserEntity userEntity);
    void updateUserLog(Long id, boolean isActive, String lastLogin, String lastLogout, int attempts, boolean isSuccess, int duration);
    int getCurrentLoggedOnUserID(Long id);
    Long getCurrentUserLogSessionID(int userID);
    boolean isUserCurrentlyLoggedIn(int userID);
    Optional<UserLogEntity> findActiveUserLogSessionByUserID(int userID);


}
