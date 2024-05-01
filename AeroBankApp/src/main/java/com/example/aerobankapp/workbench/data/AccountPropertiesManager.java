package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountPropertiesManager extends AbstractDataManager
{

    @Autowired
    public AccountPropertiesManager(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService, AccountPropertiesService accountPropertiesService, AccountCodeService accountCodeService, AccountUsersEntityService accountUsersEntityService, UserLogService userLogService) {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountCodeService, accountUsersEntityService, userLogService);
    }
}
