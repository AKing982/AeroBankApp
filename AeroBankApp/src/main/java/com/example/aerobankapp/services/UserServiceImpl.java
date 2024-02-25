package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
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
    @Transactional
    public List<UserEntity> findByUserName(String user) {

        TypedQuery<UserEntity> query = getEntityManager().createQuery("SELECT e FROM UserEntity e where e.username=:user", UserEntity.class)
                .setParameter("user", user)
                .setMaxResults(10);
        aeroLogger.debug("Found User: " + query.getResultList());

        return query.getResultList();
    }

    @Override
    public void update(UserEntity obj)
    {
        if(obj == null || obj.getUserID() < 1)
        {
            throw new IllegalArgumentException("User Entity or its ID must not be null");
        }

        UserEntity existingUserEntity = getUserRepository().findById((long) obj.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User Entity not found with id: " + obj.getUserID()));

        existingUserEntity.setUsername(obj.getUsername());
        existingUserEntity.setEmail(obj.getEmail());
        existingUserEntity.setRole(obj.getRole());
        existingUserEntity.setPassword(obj.getPassword());
        existingUserEntity.setPinNumber(obj.getPinNumber());
        existingUserEntity.setFirstName(obj.getFirstName());
        existingUserEntity.setLastName(obj.getLastName());

        getUserRepository().save(existingUserEntity);
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
        TypedQuery<Long> query = getEntityManager().createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.username = :username", Long.class);
        query.setParameter("username", user);

        long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public String generateAccountNumber(String user)
    {
        try {
            // Use SHA-256 hashing
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(user.getBytes());
            byte[] digest = md.digest();

            // Convert the hash bytes to integers and format
            int part1 = Math.abs(new BigInteger(1, new byte[]{digest[0], digest[1]}).intValue() % 100);
            int part2 = Math.abs(new BigInteger(1, new byte[]{digest[2], digest[3]}).intValue() % 100);
            int part3 = Math.abs(new BigInteger(1, new byte[]{digest[4], digest[5]}).intValue() % 100);

            return String.format("%02d-%02d-%02d", part1, part2, part3);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating account number", e);
        }
    }

}
