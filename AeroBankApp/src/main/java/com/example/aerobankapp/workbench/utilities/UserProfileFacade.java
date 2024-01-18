package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.manager.AccountManager;
import com.example.aerobankapp.manager.FeeManager;
import com.example.aerobankapp.manager.TransactionManager;
import com.example.aerobankapp.services.AccountServiceBundle;
import com.example.aerobankapp.services.BalanceHistoryDAOImpl;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.services.UserServiceBundle;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class UserProfileFacade
{
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileFacade(UserProfileService userProfileService)
    {
        this.userProfileService = userProfileService;
    }
}
