package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserDAOService
{
    private final UserRepository userRepository;

    private EntityManager entityManager;
    private AeroLogger aeroLogger = new AeroLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository repository, EntityManager manager)
    {
        this.userRepository = repository;
        this.entityManager = manager;
    }

    @Override
    @Transactional
    public List<UserEntity> findAll()
    {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(UserEntity obj)
    {
        aeroLogger.info("Saving User: " + obj);
        userRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(UserEntity obj)
    {
        aeroLogger.info("Deleting User: " + obj);
        userRepository.delete(obj);
    }

    @Override
    @Transactional
    public UserEntity findAllById(Long id)
    {
        return userRepository.findById((long)id).orElse(null);
    }

    @Override
    public List<UserEntity> findByUserName(String user) {
        TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity where username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);

        return query.getResultList();
    }


}
