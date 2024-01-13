package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.services.AccountServiceBundle;
import com.example.aerobankapp.services.BalanceHistoryDAOImpl;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.services.UserServiceBundle;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserProfileFacade
{
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileFacade(UserProfileService userProfileService)
    {
        this.userProfileService = userProfileService;
    }

    public List<CheckingAccountEntity> getCheckingAccounts(String user)
    {
        return userProfileService.getAccountServiceBundle().getCheckingService().findByUserName(user);
    }
}
