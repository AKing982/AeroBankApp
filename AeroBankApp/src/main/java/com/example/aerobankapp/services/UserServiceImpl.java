package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Getter
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private Logger aeroLogger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        getUserRepository().save(obj);
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

        TypedQuery<UserEntity> query = getEntityManager().createQuery("SELECT e FROM UserEntity e where e.username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);
        aeroLogger.debug("Found User: " + query.getResultList());

        return query.getResultList();
    }

    @Override
    public Role getUserRole(String user) throws NoResultException
    {
        TypedQuery<UserEntity> userRoleQuery = getEntityManager().createQuery("FROM UserEntity where username=:user", UserEntity.class)
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


        return userEntity;
    }

    @Override
    @Transactional
    public String getAccountNumberByUserName(String user) {
        TypedQuery<UserEntity> accountNumberQuery = getEntityManager().createQuery("FROM UserEntity WHERE username=:user", UserEntity.class);
        accountNumberQuery.setParameter("user", user);
        accountNumberQuery.setMaxResults(1);

        List<UserEntity> userEntityList = accountNumberQuery.getResultList();

        UserEntity userEntity = userEntityList.stream().findFirst().orElseThrow();

        return userEntity.getAccountNumber();
    }

    @Override
    public String getEmailByUserName(String user) {
        return null;
    }

    @Override
    public String getEmailByID(Long id) {
        return null;
    }

    @Override
    public String getPinNumberByUserName(String user) {
        return null;
    }

    @Override
    public String getPinNumberByID(Long id) {
        return null;
    }

    @Override
    public List<String> getListOfUserNames()
    {
       TypedQuery<UserEntity> typedQuery = getEntityManager().createQuery("FROM UserEntity u", UserEntity.class);
       List<UserEntity> userEntities = typedQuery.getResultList();

        return userEntities.stream().map(UserEntity::getUsername).toList();
    }

    @Override
    public int getUserIDByUserName(String user)
    {
        TypedQuery<UserEntity> typedQuery = getEntityManager().createQuery("FROM UserEntity WHERE username=: user", UserEntity.class);
        typedQuery.setParameter("user", user);
        typedQuery.setMaxResults(1);

        UserEntity userEntity = typedQuery.getSingleResult();

        return userEntity.getUserID();
    }

    @Override
    public void addUserToAccount(UserEntity entity, AccountEntity accountEntity) {
        entity.addAccount(accountEntity);
        userRepository.save(entity);
    }

    @Override
    public void removeUserFromAccount(UserEntity userEntity, AccountEntity accountEntity) {
        userEntity.removeAccount(accountEntity);
        userRepository.delete(userEntity);
    }

    @Override
    public boolean userNameExists(String user) {
        TypedQuery<UserEntity> typedQuery = getEntityManager().createQuery("FROM UserEntity u", UserEntity.class);
        List<UserEntity> userEntities = typedQuery.getResultList();
        String userName = userEntities.stream().map(UserEntity::getUsername).collect(Collectors.joining());
        return userName.equals(user);
    }

}
