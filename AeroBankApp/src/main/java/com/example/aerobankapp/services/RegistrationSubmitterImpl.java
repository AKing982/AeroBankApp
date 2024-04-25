package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.dto.SecurityQuestionsDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullAccountInfoException;
import com.example.aerobankapp.model.AccountNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegistrationSubmitterImpl implements RegistrationSubmitter
{
    private final UserService userService;
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;
    private final AccountPropertiesService accountPropertiesService;
    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationSubmitterImpl.class);

    @Autowired
    public RegistrationSubmitterImpl(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService, AccountPropertiesService accountPropertiesService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
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

    private void checkAccountInfoListForNullInput(List<AccountInfoDTO> accountInfoDTOS){
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
    public Optional<UserDTO> getUserDTOFromRegistration(RegistrationDTO registrationDTO) {
        return Optional.empty();
    }

    @Override
    public List<SecurityQuestionsDTO> getSecurityQuestionsFromRegistration(RegistrationDTO registrationDTO) {
        return null;
    }

    @Override
    public void register() {

    }

    public List<AccountEntity> getAccountEntitiesList(List<AccountInfoDTO> accountInfoDTOS){
        return null;
    }

}
