package com.example.aerobankapp.services;

import com.example.aerobankapp.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Service
public class UserProfileService
{
    private UserServiceBundle userServiceBundle;
    private AccountServiceBundle accountServiceBundle;
    private FeesRepository feesRepository;
    private InvestmentRepository investmentRepository;
    private String user;

    @Autowired
    public UserProfileService(UserServiceBundle userBundle, AccountServiceBundle accountBundle)
    {
        this.userServiceBundle = userBundle;
        this.accountServiceBundle = accountBundle;
    }

    public UserProfileService(String user)
    {
        this.user = user;
        initializeServices();
    }

    public void initializeServices()
    {

    }


}
