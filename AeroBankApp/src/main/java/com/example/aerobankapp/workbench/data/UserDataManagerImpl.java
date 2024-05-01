package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDataManagerImpl extends AbstractDataManager
{
    private final AccountNumberGenerator accountNumberGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDataManagerImpl.class);

    @Autowired
    public UserDataManagerImpl(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService,
                               AccountPropertiesService accountPropertiesService,
                               AccountCodeService accountCodeService,
                               AccountUsersEntityService accountUsersEntityService,
                               UserLogService userLogService,
                               AccountNumberGenerator accountNumberGenerator)
    {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountCodeService, accountUsersEntityService, userLogService);
        this.accountNumberGenerator = accountNumberGenerator;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public AccountNumber buildAccountNumber(String user){
        return accountNumberGenerator.generateAccountNumber(user);
    }

   private String getEncryptedString(String param){
        return bCryptPasswordEncoder.encode(param);
   }

   public List<AccountEntity> getAccountsByUserID(int userID){
        if(userID < 1){
            throw new InvalidUserIDException("Caught invalid UserID: " + userID);
        }
        List<AccountEntity> accountEntities = accountService.getListOfAccountsByUserID(userID);
        return accountEntities;
   }

   public List<UserLogEntity> getUserLogsByUserID(int userID){
        return null;
   }

   public UserEntity findUser(int id){
        return null;
   }

    public UserEntity buildUserEntity(User user, AccountNumber accountNumber){
        return null;
    }

    private UserEntity getUserEntityBuilder(UserDTO user, AccountNumber accountNumber){
        return UserEntity.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .username(user.userName())
                .email(user.email())
                .password(user.password())
                .pinNumber(user.pinNumber())
                .accountNumber(accountNumber.getAccountNumberToString())
                .isEnabled(true)
                .build();
    }

    public UserEntity buildSimpleUserEntity(User user, int currentUserID){
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(getEncryptedString(user.getPassword()))
                .pinNumber(getEncryptedString(user.getPinNumber()))
                .accountNumber(user.getAccountNumber().getAccountNumberToString())
                .isEnabled(true)
                .userID(currentUserID)
                .build();
    }

    public boolean updateUser(UserEntity user){
        try
        {
            userService.update(user);
            return true;

        }catch(Exception e){
            return false;
        }
    }

    public void saveUser(UserEntity user){
        userService.save(user);
    }

    public void deleteUser(UserEntity user){
        userService.delete(user);
    }

    public boolean createUser(User user)
    {
       return false;
    }

    public Integer modifyUser(User user){

        // Retrieve existing user data by the username
        Optional<UserEntity> optionalUserEntity = userService.findByUser(user.getUsername());
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();

            // get the userID
            int userID = userEntity.getUserID();

            // Convert the User model to an Entity
            UserEntity user1 = buildSimpleUserEntity(user, userID);

            // persist the updated properties
            updateUser(user1);
        }
        return 0;
    }

    public boolean cascadeDeleteAllUserData(int userID){
        return false;
    }

    public boolean deleteUser(int userID){
        return false;
    }
}
