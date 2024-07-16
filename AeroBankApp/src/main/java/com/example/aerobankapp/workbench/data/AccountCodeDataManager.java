package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountCodeDataManager extends AbstractDataManager
{
    private AccountCodeCreator accountCodeCreator;

    public AccountCodeDataManager(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService, AccountPropertiesService accountPropertiesService, AccountNotificationService accountNotificationService, PlaidLinkService plaidLinkService, AccountCodeService accountCodeService, AccountUsersEntityService accountUsersEntityService, UserLogService userLogService) {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountNotificationService, accountCodeService, plaidLinkService, accountUsersEntityService, userLogService);
    }


    public AccountCode createAccountCode(){
        return null;
    }

    public Integer deleteAccountCode(){
        return 0;
    }
}
