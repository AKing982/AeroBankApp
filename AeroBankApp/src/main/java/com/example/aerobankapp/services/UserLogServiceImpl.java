package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class UserLogServiceImpl implements UserLogService
{
    private final UserLogRepository userLogRepository;

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

        return null;
    }

    @Override
    public UserLogEntity getUserLogByNumberOfLoginAttempts(int attempts, int userID) {
        return null;
    }

    @Override
    public Optional<UserLogEntity> findUserLogEntriesByActiveStateAndUserID(boolean isActive, int userID) {
        return null;
    }

    @Override
    public List<UserLogEntity> getUserLogsByLastLogin(Long id, LocalDateTime lastLogin) {
        return null;
    }

    @Override
    public void updateLastLogout(Long id, LocalDateTime time) {

    }

    @Override
    public void updateLastLogin(Long id, LocalDateTime time) {

    }

    @Override
    public void updateSessionDuration(Long id, int duration) {

    }

    @Override
    public void updateIsActiveState(Long id, boolean isActive) {

    }

    @Override
    public void updateLoginAttempts(Long id, int attempts) {

    }

    @Override
    public void updateUser(Long id, UserEntity userEntity) {

    }

    @Override
    public int getCurrentLoggedOnUserID(Long id) {
        return 0;
    }

    @Override
    public boolean isUserCurrentlyLoggedIn(int userID) {
        return false;
    }

}
