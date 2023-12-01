package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.entity.SavingsAccount;
import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class UserProfileService
{
    private AccountServiceBundle accountServiceBundle;
    private UserServiceBundle userServiceBundle;
    private BalanceHistoryDAOImpl balanceHistoryService;

    @Autowired
    public UserProfileService(AccountServiceBundle accountServiceBundle,
                              UserServiceBundle userBundle,
                              BalanceHistoryDAOImpl balanceService)
    {
        this.balanceHistoryService = balanceService;
        this.accountServiceBundle = accountServiceBundle;
        this.userServiceBundle = userBundle;
    }

    public List<CheckingAccount> getCheckingAccounts(String user)
    {
        return accountServiceBundle.getCheckingService().findByUserName(user);
    }

    public List<SavingsAccount> getSavingsAccounts(String user)
    {
        return accountServiceBundle.getSavingsService().findByUserName(user);
    }

    public List<UserLog> getUserLogData(String user)
    {
        return userServiceBundle.getUserLogService().findByUserName(user);
    }

    public void insertUserLog(UserLog userLog)
    {
        userServiceBundle.getUserLogService().save(userLog);
    }
}
