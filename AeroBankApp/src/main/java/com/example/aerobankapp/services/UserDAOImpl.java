package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger aeroLogger = LoggerFactory.getLogger(UserDAOImpl.class);

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
    public Optional<UserEntity> findAllById(Long id)
    {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public List<UserEntity> findByUserName(String user) {
        TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity where username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);
        aeroLogger.debug("Found User: " + query.getResultList());

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

    @Override
    public UserEntity registerUser(RegistrationDTO registrationDTO)
    {
        UserEntity userEntity = UserEntity.builder()
                .username(registrationDTO.userName())
                .password(registrationDTO.password())
                .email(registrationDTO.email())
                .pinNumber(registrationDTO.PIN())
                .build();

        save(userEntity);
        return userEntity;
    }

}
