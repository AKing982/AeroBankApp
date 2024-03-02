package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class UserLogServiceImpl implements UserLogService
{
    private final UserLogRepository userLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserLogServiceImpl(UserLogRepository userLogRepository, EntityManager entityManager)
    {
        this.userLogRepository = userLogRepository;
        this.entityManager = entityManager;
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
    public int updateUserLog(UserLogEntity userLogEntity) {
        return 0;
    }

    @Override
    public String getIPAddressById(int id) {
        return null;
    }

    @Override
    public String getSessionToken(int id) {
        return null;
    }
}
