package com.example.aerobankapp.services;



import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.dto.SecurityQuestionsDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface RegistrationSubmitter
{
    List<AccountInfoDTO> getListOfAccountInfos(RegistrationDTO registrationDTO);

    Optional<User> getUserFromRegistration(RegistrationDTO registrationDTO);

    List<SecurityQuestionsDTO> getSecurityQuestionsFromRegistration(RegistrationDTO registrationDTO);

    AccountCode getGeneratedAccountCode(User userDTO, AccountInfoDTO accountInfoDTO);

    void register(RegistrationDTO registrationDTO);

}
