package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidAccountNumberException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public Optional<UserEntity> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public List<UserEntity> findByUserName(String user) {

        List<UserEntity> userEntityList = userRepository.findByUserName(user);
        aeroLogger.debug("Found User: " + userEntityList.get(0));
        return userEntityList;
    }

    @Override
    public void update(UserEntity obj)
    {
        if(obj == null || obj.getUserID() < 1)
        {
            throw new IllegalArgumentException("User Entity or its ID must not be null");
        }
        userRepository.updateUser(obj.getUserID(), obj.getUsername(), obj.getEmail(), obj.getRole(), obj.getPassword(), obj.getPinNumber(), obj.getFirstName(), obj.getLastName());
    }

    @Override
    public Role getUserRole(String user) throws NoResultException
    {
       return userRepository.getUserRole(user);
    }


    @Override
    @Transactional
    public String getAccountNumberByUserName(String user) {
       return userRepository.getAccountNumberByUserName(user);
    }

    @Override
    public int getUserIDByAccountNumber(String accountNumber) {
        if(accountNumber.isEmpty()){
            throw new InvalidAccountNumberException("Invalid AccountNumber caught: " + accountNumber);
        }
        return userRepository.findUserIDByAccountNumber(accountNumber);
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
    public String getUsersFullNameById(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID caught: " + userID);
        }
        return userRepository.findFullNameByUserID(userID);
    }

    @Override
    public String getPinNumberByID(Long id) {
        return null;
    }

    @Override
    public List<String> getListOfUserNames()
    {
       return userRepository.getListOfUsers();
    }

    @Override
    public int getUserIDByUserName(String user)
    {
        return userRepository.getIDByUserName(user);
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
        int count = userRepository.userExists(user);
        if(count == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean doesAccountNumberExist(final String accountNumber) {
        if(!accountNumber.isEmpty()){
            return userRepository.doesAccountNumberExist(accountNumber);
        }
        return false;
    }
}
