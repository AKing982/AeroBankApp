package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public List<Users> findAll()
    {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Users obj)
    {
        aeroLogger.info("Saving User: " + obj);
        userRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(Users obj)
    {
        aeroLogger.info("Deleting User: " + obj);
        userRepository.delete(obj);
    }

    @Override
    @Transactional
    public Users findAllById(Long id)
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
