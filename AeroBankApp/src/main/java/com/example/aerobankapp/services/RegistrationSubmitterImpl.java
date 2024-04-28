package com.example.aerobankapp.services;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.dto.SecurityQuestionsDTO;
import com.example.aerobankapp.entity.*;
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
    private final AccountUsersEntityService accountUsersEntityService;
    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationSubmitterImpl.class);

    @Autowired
    public RegistrationSubmitterImpl(UserService userService, AccountService accountService,
                                     AccountSecurityService accountSecurityService,
                                     AccountPropertiesService accountPropertiesService,
                                     AccountCodeCreator accountCodeCreator,
                                     AccountCodeService accountCodeService,
                                     AccountUsersEntityService accountUsersEntityService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
        this.accountCodeCreator = accountCodeCreator;
        this.accountCodeService = accountCodeService;
        this.accountUsersEntityService = accountUsersEntityService;
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
        User user = getUserFromRegistration(registrationDTO)
                .orElseThrow(() -> new IllegalArgumentException("User data is incomplete."));

        // Register the user
        userService.registerUser(user);

        // Retrieve the UserEntity based on the username
        UserEntity userEntity = userService.findByUser(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("User registration failed; user could not be retrieved post-registration."));
        // Retrieve the Account Data
        List<AccountInfoDTO> accountInfoDTOS = getListOfAccountInfos(registrationDTO);
        // Get the list of AccountCodes
        List<AccountCode> accountCodes = accountCodeCreator.generateListOfAccountCodes(user, accountInfoDTOS);
        List<AccountCodeEntity> accountCodeEntities = accountCodeService.getAccountCodeEntityList(accountCodes, userEntity);
        // Save all AccountCodeEntities
        accountCodeService.saveAll(accountCodeEntities);

        // Process accounts and their security using the consolidated service method
        List<AccountEntity> accountEntities = accountService.processAccountAndSecurity(accountCodeEntities, accountInfoDTOS, userEntity);
        List<AccountPropertiesEntity> accountPropertiesEntities = accountPropertiesService.getListOfAccountPropertiesFromAccountEntity(accountEntities);
        accountPropertiesService.saveAll(accountPropertiesEntities);
        List<AccountUserEntity> accountUserEntityList = accountUsersEntityService.getAccountUserEntityList(accountEntities, userEntity);

        LOGGER.info("Account Users Entity List Size: " + accountUserEntityList.size());

        // Persist the AccountUsersEntity
        accountUsersEntityService.saveAll(accountUserEntityList);
    }


    private List<AccountEntity> convertAccountDTOToEntityList(List<AccountCodeEntity> accountCodeEntities, List<AccountInfoDTO> accountInfoDTOS,UserEntity user){
        List<AccountEntity> accountEntities = new ArrayList<>();
        if (accountInfoDTOS.isEmpty() || accountCodeEntities.isEmpty()) {
            throw new NonEmptyListRequiredException("Caught empty list of Account data.");
        }
        if (accountInfoDTOS.size() != accountCodeEntities.size()) {
            throw new IllegalArgumentException("The sizes of account info DTOs, account code entities, and account security entities must match.");
        }

        for (int i = 0; i < accountInfoDTOS.size(); i++) {
            AccountInfoDTO accountInfoDTO = accountInfoDTOS.get(i);
            AccountCodeEntity accountCodeEntity = accountCodeEntities.get(i);

            if (accountInfoDTO != null && accountCodeEntity != null) {
                if (!isAnyFieldNull(accountInfoDTO)) {
                    AccountEntity account = accountService.buildAccountEntity(accountInfoDTO, accountCodeEntity, user);
                    accountEntities.add(account);
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
