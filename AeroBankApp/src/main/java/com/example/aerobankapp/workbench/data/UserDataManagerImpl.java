package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.InvalidUserDataException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.UserAlreadyExistsException;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import com.example.aerobankapp.workbench.utilities.dbUtils.DatabaseUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDataManagerImpl extends AbstractDataManager
{
    private final AccountNumberGenerator accountNumberGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DatabaseUtilities databaseUtilities;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDataManagerImpl.class);

    @Autowired
    public UserDataManagerImpl(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService,
                               AccountPropertiesService accountPropertiesService,
                               AccountNotificationService accountNotificationService,
                               AccountCodeService accountCodeService,
                               AccountUsersEntityService accountUsersEntityService,
                               UserLogService userLogService,
                               AccountNumberGenerator accountNumberGenerator,
                               DatabaseUtilities databaseUtilities)
    {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountNotificationService, accountCodeService, accountUsersEntityService, userLogService);
        this.accountNumberGenerator = accountNumberGenerator;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.databaseUtilities = databaseUtilities;
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

   public List<AccountCodeEntity> getUserAccountCodeList(int userID){
        assertUserIDNonZero(userID);
        List<AccountCodeEntity> accountCodeEntities = accountCodeService.getAccountCodesListByUserID(userID);
       LOGGER.info("Account Code Entities: " + accountCodeEntities.stream()
               .map(Object::toString)
               .collect(Collectors.joining(", ")));
       return accountCodeEntities;
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
                .userDetails(UserDetails.builder().firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).accountNumber(accountNumber.getAccountNumberToString()).build())
                .userCredentials(UserCredentials.builder().password(user.getPassword()).username(user.getUsername()).build())
                .userSecurity(UserSecurity.builder().role(user.getRole()).pinNumber(user.getPinNumber()).isEnabled(true).build())
                .build();
    }

    public UserEntity buildSimpleUserEntity(User user){
        return UserEntity.builder()
                .userDetails(UserDetails.builder().firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).accountNumber(user.getAccountNumber().getAccountNumberToString()).build())
                .userCredentials(UserCredentials.builder().password(getEncryptedString(user.getPassword())).username(user.getUsername()).build())
                .userSecurity(UserSecurity.builder().role(user.getRole()).pinNumber(getEncryptedString(user.getPinNumber())).isEnabled(true).build())
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

            deleteData(user);
            return true;

        }catch(Exception e)
        {
            LOGGER.error("An error has occurred while deleting the user: " + e.getMessage());
            return false;
        }
    }

    public void deleteAccounts(UserEntity user){
        List<AccountEntity> accountEntities = accountService.getListOfAccountsByUserID(user.getUserID());
        for(AccountEntity accountEntity : accountEntities){
            deleteAccount(accountEntity);
            LOGGER.info("Deleted account ID: {}", accountEntity.getAcctID());
        }
    }

    public void deleteAccount(AccountEntity account){
        accountSecurityService.deleteAll(account.getSecurities());
        accountPropertiesService.deleteAll(account.getAccountPropertiesEntities());

        accountService.delete(account);
    }

    public void handleAccountUserEntityDeletion(UserEntity user){

        // Delete AccountUser entities
        accountUsersEntityService.getAccountUserEntityListByUserID(user.getUserID())
                .forEach(accountUserEntity -> {
                    accountUsersEntityService.delete(accountUserEntity);
                    LOGGER.info("Deleted account user entity for user ID: {}", user.getUserID());
                });
        LOGGER.info("Deleted all account user entities.");
    }

    public void handleAccountNotificationDeletion(UserEntity user){
        // Follow similar patterns for other entities
        accountNotificationService.getAccountNotificationsByUserID(user.getUserID())
                .forEach(notification -> {
                    accountNotificationService.delete(notification);
                    LOGGER.info("Deleted account notification entity for user ID: {}", user.getUserID());
                });

        LOGGER.info("Deleted all account notification entities.");
    }

    public void handleAccountPropertiesDeletion(UserEntity user){
        // Continue with other services
        accountPropertiesService.getAccountPropertiesByUserID(user.getUserID())
                .forEach(prop -> {
                    accountPropertiesService.delete(prop);
                    LOGGER.info("Deleted account property entity for user ID: {}", user.getUserID());
                });

        LOGGER.info("Deleted all account properties entities.");
    }

    public void handleAccountSecurityDeletion(UserEntity user){

        accountSecurityService.getAccountSecurityListByUserID(user.getUserID())
                .forEach(security -> {
                    accountSecurityService.delete(security);
                    LOGGER.info("Deleted account security entity for user ID: {}", user.getUserID());
                });
        LOGGER.info("Deleted all account security entities.");
    }

    public void handleAccountDeletion(UserEntity user){
        accountService.getListOfAccountsByUserID(user.getUserID())
                .forEach(account -> {
                    accountService.delete(account);
                    LOGGER.info("Deleted account entity for user ID: {}", user.getUserID());
                });

        LOGGER.info("Deleted all account entities.");
    }

    public void handleAccountCodeDeletion(UserEntity user){
        accountCodeService.getAccountCodesListByUserID(user.getUserID())
                .forEach(code -> {
                    accountCodeService.delete(code);
                    LOGGER.info("Deleted account code entity for user ID: {}", user.getUserID());
                });
        LOGGER.info("Deleted all account code entities.");
    }

    public void handleUserLogDeletion(UserEntity user){
        userLogService.getUserLogListByUserID(user.getUserID())
                .forEach(log -> {
                    userLogService.delete(log);
                    LOGGER.info("Deleted User Log entities for userID: {}", user.getUserID());
                });
        LOGGER.info("Deleted All userLog entities.");
    }

    public void handleUserDeletion(UserEntity user){
        userService.delete(user);
        LOGGER.info("Deleted user entity for user ID: {}", user.getUserID());
    }

    public void performDeleteOperations(UserEntity user){
        handleAccountUserEntityDeletion(user);

        handleAccountNotificationDeletion(user);

        handleAccountPropertiesDeletion(user);

        handleAccountSecurityDeletion(user);

        handleAccountDeletion(user);

        handleAccountCodeDeletion(user);

        deleteAccounts(user);

        handleUserLogDeletion(user);

        handleUserDeletion(user);
    }

    public void deleteData(UserEntity user) {

        try{
            LOGGER.info("Starting to delete all user-related data for user ID: {}", user.getUserID());

            // Delete AccountUser entities
            performDeleteOperations(user);

            databaseUtilities.resetAutoIncrementsForTables();

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
