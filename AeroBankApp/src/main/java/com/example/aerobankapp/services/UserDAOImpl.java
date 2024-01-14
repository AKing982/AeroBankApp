package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class UserDAOImpl implements UserDAO
{
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private AeroLogger aeroLogger = new AeroLogger(UserDAOImpl.class);

    @Autowired
    public UserDAOImpl(UserRepository repository, EntityManager manager)
    {
        this.userRepository = repository;
        this.entityManager = manager;
    }

    @Override
    @Transactional
    public List<UserEntity> findAll()
    {
        try
        {
            return userRepository.findAll();

        }catch(ArrayIndexOutOfBoundsException ex)
        {
            aeroLogger.error("ArrayIndexOutOfBoundsException occured: " + ex.getMessage(), ex);
        }
        return null;
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
    public Optional<UserEntity> findAllById(Long id)
    {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> findByUserName(String user) {
        TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity where username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);

        return query.getResultList();
    }

    @Override
    public Role getUserRole(String user) throws NoResultException
    {
        TypedQuery<UserEntity> userRoleQuery = entityManager.createQuery("FROM UserEntity where username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);

        UserEntity userEntity = userRoleQuery.getSingleResult();
        return userEntity.getRole();
    }

}
