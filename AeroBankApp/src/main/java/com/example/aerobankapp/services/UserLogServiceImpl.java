package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void updateLastLogout(Long id, LocalDateTime time) {
        LOGGER.info("Last Logout: " + time);
        userLogRepository.updateLastLogout(time, id);
    }

    @Override
    public void updateLastLogin(Long id, LocalDateTime time) {
        userLogRepository.updateLastLogin(time, id);
    }

    @Override
    public void updateSessionDuration(Long id, int duration) {
        userLogRepository.updateSessionDuration(duration, id);
    }

    @Override
    public void updateIsActiveState(Long id, boolean isActive) {
        userLogRepository.updateIsActiveState(isActive, id);
    }

    @Override
    public void updateLoginAttempts(Long id, int attempts) {
        userLogRepository.updateLoginAttempts(attempts, id);
    }

    @Override
    public void updateUser(Long id, UserEntity userEntity) {
        userLogRepository.updateUser(userEntity, id);
    }

    @Override
    public int getCurrentLoggedOnUserID(Long id) {
        if(id < 1){
            throw new IllegalArgumentException("Invalid User Log id found.");
        }
        return userLogRepository.getCurrentLoggedOnUser(id);
    }

    @Override
    public boolean isUserCurrentlyLoggedIn(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID Found.");
        }
        return userLogRepository.isUserCurrentlyLoggedIn(userID);
    }

}
