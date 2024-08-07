package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class UserLogServiceImpl implements UserLogService
{
    private final UserLogRepository userLogRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(UserLogServiceImpl.class);

    @Autowired
    public UserLogServiceImpl(UserLogRepository userLogRepository)
    {
        this.userLogRepository = userLogRepository;
    }

    @Override
    public List<UserLogEntity> findAll() {
        return userLogRepository.findAll();
    }

    @Override
    public void save(UserLogEntity obj)
    {
        if(obj == null)
        {
            throw new IllegalArgumentException("Unable to save Invalid UserLog...");
        }
        userLogRepository.save(obj);
    }

    @Override
    public void delete(UserLogEntity obj)
    {
        if(obj == null)
        {
            throw new IllegalArgumentException("Unable to delete the UserLog: " + obj);
        }
        userLogRepository.delete(obj);
    }

    @Override
    public Optional<UserLogEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserLogEntity> findByUserName(String user) {
        if(user.isEmpty()){
            throw new IllegalArgumentException("UserName cannot be empty.");
        }
        return userLogRepository.findByUserName(user);
    }

    @Override
    public UserLogEntity getUserLogByNumberOfLoginAttempts(int attempts, int userID) {
        if(attempts < 0 || userID < 1){
            throw new IllegalArgumentException("Invalid Login Attempts or UserID found.");
        }

        UserLogEntity userLogEntity = userLogRepository.getUserLogByLoginAttemptsAndUserID(attempts, userID);
        if(userLogEntity == null){
            throw new IllegalArgumentException("No UserLogs found in the database.");
        }
        return userLogEntity;
    }

    @Override
    public List<UserLogEntity> getUserLogListByUserID(int userID) {
        return userLogRepository.findUserLogEntitiesByUserID(userID);
    }

    @Override
    public Optional<UserLogEntity> findUserLogEntriesByActiveStateAndUserID(boolean isActive, int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID Found.");
        }
        return userLogRepository.findUserLogEntriesByActiveStateAndUserID(isActive, userID);
    }

    @Override
    public List<UserLogEntity> getUserLogsByLastLogin(Long id, LocalDateTime lastLogin) {
        if(id < 1 || lastLogin == null){
            throw new IllegalArgumentException("Illegal Parameters.");
        }
        return userLogRepository.getUserLogsByLastLogin(id, lastLogin);
    }

    @Override
    public void updateUser(Long id, UserEntity userEntity) {
        userLogRepository.updateUser(userEntity, id);
    }

    @Override
    @Transactional
    public void updateUserLog(Long id, boolean isActive, String lastLogin, String lastLogout, int attempts, boolean isSuccess, int duration) {
        userLogRepository.updateUserLog(isActive, lastLogin, lastLogout, attempts, isSuccess, duration, id);
    }

    @Override
    public int getCurrentLoggedOnUserID(final Long id) {
        if(id < 1){
            throw new IllegalArgumentException("Invalid User Log id found.");
        }
        return userLogRepository.getCurrentLoggedOnUser(id);
    }

    @Override
    public Long getCurrentUserLogSessionID(final int userID) {
        if(userID >= 1){
            return userLogRepository.getCurrentUserLogSessionID(userID);
        }
        return null;
    }

    @Override
    public boolean isUserCurrentlyLoggedIn(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID Found.");
        }
        return userLogRepository.isUserCurrentlyLoggedIn(userID);
    }

    @Override
    public Optional<UserLogEntity> findActiveUserLogSessionByUserID(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID has been found.");
        }
        Optional<UserLogEntity> userLogEntity = userLogRepository.findActiveUserLogSessionByUserID(userID);
        if(userLogEntity.isEmpty()){
            throw new EntityNotFoundException("Unable to retrieve UserLog for userID: " + userID + " from the database.");
        }
        return userLogRepository.findActiveUserLogSessionByUserID(userID);
    }

}
