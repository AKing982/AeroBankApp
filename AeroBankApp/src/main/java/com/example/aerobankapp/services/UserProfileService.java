package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.entity.SavingsAccount;
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
@Builder
@Service
public class UserProfileService
{
    private AccountServiceBundle accountServiceBundle;

    @Autowired
    public UserProfileService(AccountServiceBundle accountServiceBundle)
    {
        this.accountServiceBundle = accountServiceBundle;
    }

    public List<CheckingAccount> getCheckingAccounts(String user)
    {
        return accountServiceBundle.getCheckingService().findByUserName(user);
    }


    public List<SavingsAccount> getSavingsAccounts(String user)
    {
        return accountServiceBundle.getSavingsService().findByUserName(user);
    }
}
