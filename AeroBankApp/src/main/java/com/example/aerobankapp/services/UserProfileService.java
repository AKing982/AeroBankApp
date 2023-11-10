package com.example.aerobankapp.services;

import com.example.aerobankapp.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserProfileService
{
    private UserServiceImpl userService;
    private UserLogServiceImpl userLogService;
    private SavingsAccountServiceImpl SavingsService;
    private FeesRepository feesRepository;
    private CheckingRepositoryServiceImpl checkingService;
    private InvestmentRepository investmentRepository;
    private String user;

    public UserProfileService(String user)
    {
        this.user = user;
        initializeServices();
    }

    public void initializeServices()
    {

    }


}
