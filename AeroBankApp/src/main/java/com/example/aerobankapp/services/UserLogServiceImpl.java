package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserLogServiceImpl implements UserLogService
{
    private UserLogRepository userLogRepository;

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
        return null;
    }

    @Override
    public void save(UserLogEntity obj) {

    }

    @Override
    public void delete(UserLogEntity obj) {

    }

    @Override
    public Optional<UserLogEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserLogEntity> findByUserName(String user) {
        return null;
    }
}
