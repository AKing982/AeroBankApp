package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserDAOService
{
    @PersistenceContext
    private EntityManager entityManager;
    private UserRepository userRepository;
    private AeroLogger aeroLogger = new AeroLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository repository, EntityManager manager)
    {
        this.userRepository = repository;
        this.entityManager = manager;
    }

    @Override
    @Transactional
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Users obj)
    {
        userRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(Users obj)
    {
        userRepository.delete(obj);
    }

    @Override
    @Transactional
    public Users findAllById(int id)
    {
        return userRepository.findById((long)id).orElse(null);
    }

    @Override
    public List<Users> findByUserName(String user) {
        TypedQuery<Users> query = entityManager.createQuery("FROM Users where username=:user", Users.class)
                .setParameter("user", user)
                .setMaxResults(10);

        return query.getResultList();
    }


}
