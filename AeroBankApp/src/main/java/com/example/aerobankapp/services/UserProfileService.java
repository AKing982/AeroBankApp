package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class UserProfileService
{
    private UserServiceBundle userServiceBundle;
    private AccountServiceBundle accountServiceBundle;
    private BalanceHistoryDAOImpl balanceHistoryDAO;

    @Autowired
    public UserProfileService(UserServiceBundle userServiceBundle,
                              AccountServiceBundle accountServiceBundle,
                              BalanceHistoryDAOImpl balanceHistoryDAO)
    {
        this.userServiceBundle = userServiceBundle;
        this.accountServiceBundle = accountServiceBundle;
        this.balanceHistoryDAO = balanceHistoryDAO;
    }

    public List<AccountEntity> getAccountsByUser(String user)
    {
        return getAccountServiceBundle().getAccountsByUserName(user);
    }

    public void saveAccount(AccountEntity accountEntity)
    {
        getAccountServiceBundle().saveAccount(accountEntity);
    }

    public void deleteAccount(AccountEntity accountEntity)
    {
        getAccountServiceBundle().deleteAccount(accountEntity);
    }

    public List<UserEntity> getUser()
    {
        return null;
    }


}
