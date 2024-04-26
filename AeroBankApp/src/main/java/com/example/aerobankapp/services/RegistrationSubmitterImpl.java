package com.example.aerobankapp.services;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.dto.SecurityQuestionsDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationSubmitterImpl implements RegistrationSubmitter
{
    private final UserService userService;
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;
    private final AccountPropertiesService accountPropertiesService;
    private final AccountCodeCreator accountCodeCreator;
    private final AccountCodeService accountCodeService;
    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationSubmitterImpl.class);

    @Autowired
    public RegistrationSubmitterImpl(UserService userService, AccountService accountService,
                                     AccountSecurityService accountSecurityService,
                                     AccountPropertiesService accountPropertiesService,
                                     AccountCodeCreator accountCodeCreator,
                                     AccountCodeService accountCodeService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
        this.accountCodeCreator = accountCodeCreator;
        this.accountCodeService = accountCodeService;
    }


    @Override
    public List<AccountInfoDTO> getListOfAccountInfos(final RegistrationDTO registrationDTO) {
        List<AccountInfoDTO> accountInfoDTOS = registrationDTO.accounts();
        if(accountInfoDTOS.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to create accounts due to empty or missing accounts.");
        }
        // Check for Null Data
        checkAccountInfoListForNullInput(accountInfoDTOS);

        return accountInfoDTOS;
    }

    private void checkAccountInfoListForNullInput(final List<AccountInfoDTO> accountInfoDTOS){
        accountInfoDTOS.stream()
                .filter(Objects::nonNull)
                .filter(this::isAnyFieldNull)
                .forEach(accountInfoDTO -> {
                    LOGGER.error("Found Null Account Info Parameters. Account Name: {}, Account Type: {}, Initial Balance: ${}",
                            accountInfoDTO.accountName(),
                            accountInfoDTO.accountType(),
                            accountInfoDTO.initialBalance());
                    throw new NullAccountInfoException("Found Null AccountInfo params in list. Unable to proceed in creating accounts.");
                });
    }

    private boolean isAnyFieldNull(AccountInfoDTO accountInfoDTO){
        return accountInfoDTO.accountName() == null ||
                accountInfoDTO.accountType() == null ||
                accountInfoDTO.initialBalance() == null;
    }

    @Override
    public Optional<User> getUserFromRegistration(RegistrationDTO registrationDTO) {
        if(registrationDTO == null){
            throw new NullRegistrationDTOException("Unable to create User Object from null Registration...");
        }
        return Optional.of(buildUser(registrationDTO));
    }

    @Override
    public List<SecurityQuestionsDTO> getSecurityQuestionsFromRegistration(RegistrationDTO registrationDTO) {
        return null;
    }

    @Override
    public AccountCode getGeneratedAccountCode(User userDTO, AccountInfoDTO accountInfoDTO) {
        return accountCodeCreator.generateAccountCode(userDTO, accountInfoDTO);
    }

    @Override
    public void register(RegistrationDTO registrationDTO) {

        // Retrieve the User Data from the registration
        Optional<User> userData = getUserFromRegistration(registrationDTO);

        // Retrieve the Account Data
        List<AccountInfoDTO> accountInfoDTOS = getListOfAccountInfos(registrationDTO);

        // Retrieve the security questions
        List<SecurityQuestionsDTO> securityQuestionsDTOS = getSecurityQuestionsFromRegistration(registrationDTO);

        // Retrieve the User
        User user = userData.orElse(null);

        // Register the user
        userService.registerUser(user);

        // Build the AccountEntityList
        List<AccountEntity> accountEntities = convertAccountDTOToEntityList(accountInfoDTOS, user);

        // Persist the accountList
        accountService.saveAll(accountEntities);

        // Retrieve the AccountID's for the account's just saved to the database
        Set<Integer> accountIDSet = getAccountIDSet(accountEntities);

        // Create the AccountSecurity objects for each account
        List<AccountSecurityEntity> accountSecurityEntities = accountSecurityService.getAccountSecurityEntityListFromAccounts(accountEntities);

        // Save the AccountSecurity objects
        accountSecurityService.saveAll(accountSecurityEntities);

        // Create the AccountProperties objects for each account
    }


    private Set<Integer> getAccountIDSet(List<AccountEntity> accountEntities){
        return accountEntities.stream()
                .filter(Objects::nonNull)
                .map(AccountEntity::getAcctID)
                .collect(Collectors.toSet());
    }

    private List<AccountEntity> convertAccountDTOToEntityList(List<AccountInfoDTO> accountInfoDTOS, User user){
        List<AccountEntity> accountEntities = new ArrayList<>();
        if(accountInfoDTOS.isEmpty()){
            throw new NonEmptyListRequiredException("Caught empty list of Account data.");
        }

        for(AccountInfoDTO accountInfoDTO : accountInfoDTOS){
            if(accountInfoDTO != null){
                if(!isAnyFieldNull(accountInfoDTO)){
                    AccountCode accountCode = getGeneratedAccountCode(user, accountInfoDTO);
                    if(accountCode != null){
                        String formattedAccountCode = accountCodeCreator.formatAccountCode(accountCode);
                        AccountEntity account = accountService.buildAccountEntity(accountInfoDTO, formattedAccountCode);
                        accountEntities.add(account);
                    }
                }
            }
        }
        return accountEntities;
    }


    public User buildUser(RegistrationDTO registrationDTO){

        if(assertUserAlreadyExists(registrationDTO.username())){
            throw new UserAlreadyExistsException("User with username: " + registrationDTO.username() + " already exists.");
        }

        if(assertRegistrationParametersNull(registrationDTO)){
            showRegistrationLoggerDetails(LOGGER, registrationDTO);
            throw new InvalidRegistrationParamsException("Unable to build UserDTO due to invalid parameters.");
        }
        return getUserBuilder(registrationDTO);
    }

    public boolean assertUserAlreadyExists(String user){
        Optional<UserEntity> userEntity = userService.findByUser(user);
        if(userEntity.isPresent()){
           return true;
        }
        return false;
    }

    private void showRegistrationLoggerDetails(Logger LOGGER, RegistrationDTO registrationDTO){
        LOGGER.error("Found invalid registration parameters. UserName: {}, Email: {}, FirstName: {}, LastName: {}, PIN: {}, Password: {}",
                registrationDTO.username(),
                registrationDTO.email(),
                registrationDTO.firstName(),
                registrationDTO.lastName(),
                registrationDTO.PIN(),
                registrationDTO.password());
    }

    private User getUserBuilder(RegistrationDTO registrationDTO){
        return User.builder()
                .firstName(registrationDTO.firstName())
                .lastName(registrationDTO.lastName())
                .email(registrationDTO.email())
                .password(registrationDTO.password())
                .pinNumber(registrationDTO.PIN())
                .isAdmin(registrationDTO.isAdmin())
                .username(registrationDTO.username())
                .role(registrationDTO.role())
                .build();
    }

    private boolean assertRegistrationParametersNull(RegistrationDTO registrationDTO){
        return registrationDTO.username().isEmpty() || registrationDTO.email().isEmpty() ||
                registrationDTO.firstName().isEmpty() || registrationDTO.lastName().isEmpty() ||
                registrationDTO.password().isEmpty() || registrationDTO.PIN().isEmpty();
    }
}
