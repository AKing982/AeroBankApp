package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.entity.UserLogEntity;
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
    private BalanceHistoryServiceImpl balanceHistoryService;

    @Autowired
    public UserProfileService(AccountServiceBundle accountServiceBundle,
                              UserServiceBundle userBundle,
                              BalanceHistoryServiceImpl balanceService)
    {
        this.balanceHistoryService = balanceService;
        this.accountServiceBundle = accountServiceBundle;
        this.userServiceBundle = userBundle;
    }

    public List<CheckingAccountEntity> getCheckingAccounts(String user)
    {
        return accountServiceBundle.getCheckingService().findByUserName(user);
    }

    public List<SavingsAccountEntity> getSavingsAccounts(String user)
    {
        return accountServiceBundle.getSavingsService().findByUserName(user);
    }

    public List<UserLogEntity> getUserLogData(String user)
    {
        return userServiceBundle.getUserLogService().findByUserName(user);
    }

    public void insertUserLog(UserLogEntity userLog)
    {
        userServiceBundle.getUserLogService().save(userLog);
    }
}
