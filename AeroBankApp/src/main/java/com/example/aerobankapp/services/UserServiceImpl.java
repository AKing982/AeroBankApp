package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final AccountNumberGenerator accountNumberGenerator;

    @PersistenceContext
    private EntityManager entityManager;
    private Logger aeroLogger = LoggerFactory.getLogger(UserServiceImpl.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, AccountNumberGenerator accountNumberGenerator, EntityManager manager)
    {
        this.userRepository = repository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.entityManager = manager;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
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
    public UserEntity findById(int userID) {
        Optional<UserEntity> user = userRepository.findById(userID);
        return user.orElse(null);
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
        userRepository.updateUser(obj.getUserID(), obj.getUserCredentials().getUsername(), obj.getUserDetails().getEmail(), obj.getUserSecurity().getRole(), obj.getUserCredentials().getPassword(), obj.getUserSecurity().getPinNumber(), obj.getUserDetails().getFirstName(), obj.getUserDetails().getLastName());
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
        return userRepository.findEmailByUserName(user);
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
        return userRepository.userExists(user) == 1;
    }

    @Override
    public Boolean userIDExists(int id) {
        if(id < 1)
        {
            return false;
        }
        return userRepository.userIDExists(id);
    }

    @Override
    //TODO: UNIT TEST THIS METHOD
    public boolean doesNewPasswordMatchCurrentPassword(String user, String newPassword){
        if(user.isEmpty() || newPassword.isEmpty()){
            aeroLogger.error("UserName or password is empty.");
            return false;
        }
        String currentEncodedPassword = userRepository.findUsersCurrentPassword(user);
        aeroLogger.info("Current Password: " + currentEncodedPassword);
        if(currentEncodedPassword == null || currentEncodedPassword.isEmpty()){
            aeroLogger.info("No Current password found.");
            return false;
        }
        boolean isMatch = bCryptPasswordEncoder.matches(newPassword, currentEncodedPassword);
        aeroLogger.info("Password's Match: " + isMatch);
        return isMatch;
    }

    @Override
    public boolean doesAccountNumberExist(final String accountNumber) {
        if(!accountNumber.isEmpty()){
            return userRepository.doesAccountNumberExist(accountNumber);
        }
        return false;
    }

    public UserEntity buildUserEntity(User userDTO, AccountNumber accountNumber){

        String user = userDTO.getUsername();
        if(userNameExists(user)){
            throw new UserExistsException("User already exists in the database: " + user);
        }
        aeroLogger.info("User Role: " + userDTO.getRole());
        return UserEntity.builder()
                .userDetails(UserDetails.builder().email(userDTO.getEmail()).accountNumber(accountNumber.getAccountNumberToString()).firstName(userDTO.getFirstName()).lastName(userDTO.getLastName()).build())
                .userSecurity(UserSecurity.builder().isEnabled(true).role(userDTO.getRole()).pinNumber(getEncryptedPinNumber(userDTO.getPinNumber())).isAdmin(userDTO.isAdmin()).build())
                .userCredentials(UserCredentials.builder().password(getEncryptedPassword(userDTO.getPassword())).username(userDTO.getUsername()).build())
                .build();
    }


    private String getEncryptedPinNumber(String pin){
        return bCryptPasswordEncoder.encode(pin);
    }


    private String getEncryptedPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public void registerUser(User user) {
        if (user == null) {
            throw new InvalidUserDTOException("Invalid User caught.");
        }
        // Get the Generated AccountNumber
        AccountNumber generatedAccountNumber = generateAccountNumber(user.getUsername());

        // Build the User Entity
        UserEntity userEntity = buildUserEntity(user, generatedAccountNumber);

        // Save the User Entity
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUserPassword(String password, String user) {
        userRepository.updateUserPassword(password, user);
    }

    @Override
    public AccountNumber generateAccountNumber(String user) {
        return accountNumberGenerator.generateAccountNumber(user);
    }

    @Override
    @Transactional
    public Optional<UserEntity> findByUser(String user) {
        return userRepository.fetchByUser(user);
    }




}
