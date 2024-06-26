package com.example.aerobankapp.services;
;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public interface UserService extends ServiceDAOModel<UserEntity>
{
    @Override
    List<UserEntity> findAll();

    @Override
    void save(UserEntity obj);

    @Override
    void delete(UserEntity obj);

    @Override
    Optional<UserEntity> findAllById(Long id);

    Optional<UserEntity> findById(int id);

    @Override
    List<UserEntity> findByUserName(String user);
    void update(UserEntity obj);

    Role getUserRole(String user) throws NoResultException;

    String getAccountNumberByUserName(String user);

    int getUserIDByAccountNumber(String accountNumber);

    String getEmailByUserName(String user);

    String getEmailByID(Long id);

    String getPinNumberByUserName(String user);

    String getUsersFullNameById(int userID);

    String getPinNumberByID(Long id);

    List<String> getListOfUserNames();

    int getUserIDByUserName(String user);

    void addUserToAccount(UserEntity entity, AccountEntity accountEntity);

    void removeUserFromAccount(UserEntity userEntity, AccountEntity accountEntity);

    boolean userNameExists(String user);

    Boolean userIDExists(int id);

    boolean doesNewPasswordMatchCurrentPassword(String user, String newPassword);

    boolean doesAccountNumberExist(String accountNumber);

    void registerUser(User user);

    void updateUserPassword(String password, String user);

    AccountNumber generateAccountNumber(String user);

    Optional<UserEntity> findByUser(String user);
}

