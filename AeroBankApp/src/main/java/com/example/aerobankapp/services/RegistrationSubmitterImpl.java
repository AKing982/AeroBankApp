package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.dto.SecurityQuestionsDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationSubmitterImpl implements RegistrationSubmitter
{
    private final UserService userService;
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;
    private final AccountPropertiesService accountPropertiesService;

    @Autowired
    public RegistrationSubmitterImpl(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService, AccountPropertiesService accountPropertiesService) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
    }

    @Override
    public AccountNumber getGeneratedAccountNumber(String username){
        try {
            // Use SHA-256 hashing
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(username.getBytes());
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

    public void saveUserEntity(UserEntity userEntity){

    }

    public void saveAccountEntity(AccountEntity account){

    }

    public void saveAccountPropertiesEntity(AccountPropertiesEntity accountPropertiesEntity){

    }

    public void saveAccountSecurityEntity(AccountSecurityEntity accountSecurityEntity){

    }

    public boolean validateGeneratedAccountNumber(String accountNumber){
        return false;
    }

    @Override
    public UserEntity registerUser(Optional<UserDTO> registrationDTO) {
        return null;
    }

    @Override
    public List<AccountInfoDTO> getListOfAccountInfos(RegistrationDTO registrationDTO) {
        return null;
    }

    @Override
    public Optional<UserDTO> getUserDTOFromRegistration(RegistrationDTO registrationDTO) {
        return Optional.empty();
    }

    @Override
    public List<SecurityQuestionsDTO> getSecurityQuestionsFromRegistration(RegistrationDTO registrationDTO) {
        return null;
    }

    public List<AccountEntity> getAccountEntitiesList(List<AccountInfoDTO> accountInfoDTOS){
        return null;
    }

    private AccountEntity createAccountEntity(){
        return null;
    }

    private AccountPropertiesEntity createAccountPropertiesEntity(){
        return null;
    }
}
