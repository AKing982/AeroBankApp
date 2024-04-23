package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private UserEntity buildUserEntity(RegistrationDTO registrationDTO){
        return null;
    }
    private AccountEntity buildAccountEntity(RegistrationDTO registrationDTO){
        return null;
    }
    private AccountSecurityEntity buildAccountSecurityEntity(RegistrationDTO registrationDTO){
        return null;
    }
    private AccountPropertiesEntity buildAccountPropertiesEntity(RegistrationDTO registrationDTO){
        return null;
    }
}
