package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.InvalidUserDataException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.UserAlreadyExistsException;
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
import java.util.stream.Collectors;

@Component
public class UserDataManagerImpl extends AbstractDataManager
{
    private final AccountNumberGenerator accountNumberGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDataManagerImpl.class);

    @Autowired
    public UserDataManagerImpl(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService,
                               AccountPropertiesService accountPropertiesService,
                               AccountNotificationService accountNotificationService,
                               AccountCodeService accountCodeService,
                               AccountUsersEntityService accountUsersEntityService,
                               UserLogService userLogService,
                               AccountNumberGenerator accountNumberGenerator)
    {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountNotificationService, accountCodeService, accountUsersEntityService, userLogService);
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
        assertUserIDNonZero(userID);
        List<AccountEntity> accountEntities = accountService.getListOfAccountsByUserID(userID);
        return accountEntities;
   }

   public List<AccountNotificationEntity> getAccountNotificationsByUserID(int userID){
        assertUserIDNonZero(userID);
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationService.getAccountNotificationsByUserID(userID);
        return accountNotificationEntities;
   }

   public List<AccountSecurityEntity> getUserAccountSecurityList(int userID){
        assertUserIDNonZero(userID);
        List<AccountSecurityEntity> accountSecurityEntities = accountSecurityService.getAccountSecurityListByUserID(userID);
       LOGGER.info("Account Security Entities: " + accountSecurityEntities.stream()
               .map(Object::toString)
               .collect(Collectors.joining(", ")));
        return accountSecurityService.getAccountSecurityListByUserID(userID);
   }

   public List<AccountPropertiesEntity> getUserAccountPropertiesList(int userID){
        assertUserIDNonZero(userID);
        List<AccountPropertiesEntity> accountPropertiesEntities = accountPropertiesService.getAccountPropertiesByUserID(userID);
       LOGGER.info("Account Property Entities: " + accountPropertiesEntities.stream()
               .map(Object::toString)
               .collect(Collectors.joining(", ")));
        return accountPropertiesService.getAccountPropertiesByUserID(userID);
   }

   public List<AccountUserEntity> getAccountUserList(int userID){
        assertUserIDNonZero(userID);
        return accountUsersEntityService.getAccountUserEntityListByUserID(userID);
   }

   public List<UserLogEntity> getUserLogsByUserID(int userID){
        assertUserIDNonZero(userID);
        return null;
   }

   public UserEntity findUser(int id){
        assertUserIDNonZero(id);
        Optional<UserEntity> userEntity = userService.findById(id);
        if(userEntity.isPresent()){
            return userEntity.get();
        }else{
            throw new UserNotFoundException("No User Found with ID: " + id);
        }
   }

    public UserEntity buildUserEntity(User user, AccountNumber accountNumber){
        if(user == null || accountNumber == null){
            throw new InvalidUserDataException("Unable to build user entity from null user param or account number param.");
        }
        return getUserEntityBuilder(user, accountNumber);
    }

    private UserEntity getUserEntityBuilder(User user, AccountNumber accountNumber){
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .pinNumber(user.getPinNumber())
                .role(user.getRole())
                .accountNumber(accountNumber.getAccountNumberToString())
                .isEnabled(true)
                .build();
    }

    public UserEntity buildSimpleUserEntity(User user){
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(getEncryptedString(user.getPassword()))
                .pinNumber(getEncryptedString(user.getPinNumber()))
                .accountNumber(user.getAccountNumber().getAccountNumberToString())
                .isEnabled(true)
                .role(user.getRole())
                .userID(user.getUserID())
                .build();
    }

    public void updateUser(UserEntity user){
        userService.update(user);
    }

    public void saveUser(UserEntity user){
        userService.save(user);
    }

    public void deleteUser(UserEntity user){
        userService.delete(user);
    }

    public boolean createUser(User user)
    {
        assertUserParameterNotNull(user);
        // Check if the user already exists
        boolean exists = userService.userNameExists(user.getUsername());
        if(exists){
            LOGGER.info("User: " + user.getUsername() + " already exists.");
            return false;
        }
        try
        {
            AccountNumber accountNumber = buildAccountNumber(user.getUsername());
            UserEntity userEntity = buildUserEntity(user, accountNumber);
            saveUser(userEntity);
            return true;

        }catch(Exception e){
            LOGGER.error("There was an error creating user: " + user.getUsername() + " due to : " + e.getMessage());
            return false;
        }
    }

    public boolean modifyUser(User user){
        assertUserParameterNotNull(user);
        // Retrieve existing user data by the userID
        try{
            // Build a new UserEntity based off the User
            UserEntity modifiedUserEntity = buildSimpleUserEntity(user);

            // Persist the modified entity with an update
            updateUser(modifiedUserEntity);
            return true;

        }catch(Exception e){
            LOGGER.error("There was an error while updating the user: " + e.getMessage());
            return false;
        }

    }

    private void assertUserParameterNotNull(User user){
        if(user == null){
            throw new InvalidUserDataException("Unable to create new user from null User parameter...");
        }
    }

    private void assertUserIDNonZero(int userID){
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID caught: " + userID);
        }
    }

    public boolean cascadeDeleteAllUserData(int userID){
        assertUserIDNonZero(userID);
        // Obtain User Entity data from the user ID
        try{

            UserEntity user = findUser(userID);

            // Obtain the Account Entity data
            List<AccountEntity> accountEntities = getAccountsByUserID(userID);

            // Obtain the Account Security data
            List<AccountSecurityEntity> accountSecurityEntities = getUserAccountSecurityList(userID);

            // Obtain the Account Properties data
            List<AccountPropertiesEntity> accountPropertiesEntities = getUserAccountPropertiesList(userID);

            List<AccountUserEntity> accountUserEntities = getAccountUserList(userID);

            List<AccountNotificationEntity> accountNotificationEntities = getAccountNotificationsByUserID(userID);

            deleteData(user, accountUserEntities, accountPropertiesEntities, accountNotificationEntities, accountSecurityEntities, accountEntities);
            return true;

        }catch(Exception e)
        {
            LOGGER.error("An error has occurred while deleting the user: " + e.getMessage());
            return false;
        }
    }

    private void deleteData(UserEntity user, List<AccountUserEntity> accountUserEntities,
                            List<AccountPropertiesEntity> accountPropertiesEntities,
                            List<AccountNotificationEntity> accountNotificationEntities,
                            List<AccountSecurityEntity> accountSecurityEntities, 
                            List<AccountEntity> accountEntities) {

        try{
            LOGGER.info("Starting to delete all user-related data for user ID: {}", user.getUserID());


            accountUsersEntityService.deleteAll(accountUserEntities);
            LOGGER.info("Deleted all account user entities.");

            accountPropertiesService.deleteAll(accountPropertiesEntities);
            LOGGER.info("Deleted all account properties entities.");

            accountNotificationService.deleteAll(accountNotificationEntities);
            LOGGER.info("Deleted all account notification entities.");

            accountSecurityService.deleteAll(accountSecurityEntities);
            LOGGER.info("Deleted all account security entities.");

            accountService.deleteAll(accountEntities);
            LOGGER.info("Deleted all account entities.");

            userService.delete(user);
            LOGGER.info("Deleted user entity.");

            LOGGER.info("Successfully deleted all data for user ID: {}", user.getUserID());

        }catch(Exception e){
            LOGGER.error("Error deleting data for user ID {}: {}", user.getUserID(), e.getMessage(), e);
            throw e; //
        }
    }


    public boolean deleteUser(int userID){
        return false;
    }
}
