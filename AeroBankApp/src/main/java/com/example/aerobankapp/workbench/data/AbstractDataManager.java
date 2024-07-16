package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.services.*;

public abstract class AbstractDataManager
{
    protected final UserService userService;
    protected final AccountService accountService;
    protected final AccountSecurityService accountSecurityService;
    protected final AccountPropertiesService accountPropertiesService;
    protected final AccountNotificationService accountNotificationService;
    protected final AccountCodeService accountCodeService;
    protected final PlaidLinkService plaidLinkService;
    protected final AccountUsersEntityService accountUsersEntityService;
    protected final UserLogService userLogService;

    public AbstractDataManager(UserService userService, AccountService accountService,
                               AccountSecurityService accountSecurityService,
                               AccountPropertiesService accountPropertiesService,
                               AccountNotificationService accountNotificationService,
                               AccountCodeService accountCodeService,
                               PlaidLinkService plaidLinkService,
                               AccountUsersEntityService accountUsersEntityService,
                               UserLogService userLogService)
    {
        this.userService = userService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.accountPropertiesService = accountPropertiesService;
        this.accountNotificationService = accountNotificationService;
        this.accountCodeService = accountCodeService;
        this.plaidLinkService = plaidLinkService;
        this.accountUsersEntityService = accountUsersEntityService;
        this.userLogService = userLogService;
    }
}
