package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.*;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountNumber;

import java.util.List;
import java.util.Optional;

public interface RegistrationSubmitter
{
    List<AccountInfoDTO> getListOfAccountInfos(RegistrationDTO registrationDTO);

    Optional<UserDTO> getUserDTOFromRegistration(RegistrationDTO registrationDTO);

    List<SecurityQuestionsDTO> getSecurityQuestionsFromRegistration(RegistrationDTO registrationDTO);

    void register();

}
