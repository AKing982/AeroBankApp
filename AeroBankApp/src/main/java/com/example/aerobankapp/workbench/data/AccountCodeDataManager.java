package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountCodeDataManager extends AbstractDataManager
{
    private AccountCodeCreator accountCodeCreator;

    @Autowired
    public AccountCodeDataManager(UserService userService,
                              AccountService accountService,
                              AccountSecurityService accountSecurityService,
                              AccountPropertiesService accountPropertiesService,
                              AccountCodeService accountCodeService,
                              AccountUsersEntityService accountUsersEntityService,
                              UserLogService userLogService,
                              AccountCodeCreator accountCodeCreator) {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountCodeService, accountUsersEntityService, userLogService);
        this.accountCodeCreator = accountCodeCreator;
    }

    public AccountCode createAccountCode(){
        return null;
    }

    public Integer deleteAccountCode(){
        return 0;
    }
}
