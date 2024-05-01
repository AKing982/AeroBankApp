package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.services.*;

public abstract class AbstractDataManager
{
    protected final UserService userService;
    protected final AccountService accountService;
    protected final AccountSecurityService accountSecurityService;
    protected final AccountPropertiesService accountPropertiesService;
    protected final AccountCodeService accountCodeService;
    protected final AccountUsersEntityService accountUsersEntityService;
    protected final UserLogService userLogService;

    public AbstractDataManager(UserService userService, AccountService accountService,
                               AccountSecurityService accountSecurityService,
                               AccountPropertiesService accountPropertiesService,
                               AccountCodeService accountCodeService,
                               AccountUsersEntityService accountUsersEntityService,
                               UserLogService userLogService)
    {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
        this.accountCodeService = accountCodeService;
        this.accountUsersEntityService = accountUsersEntityService;
        this.userLogService = userLogService;
    }
}
