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
    private UserLogRepository userLogRepository;
    private SavingsRepository savingsRepository;
    private FeesRepository feesRepository;
    private CheckingRepository checkingRepository;
    private InvestmentRepository investmentRepository;

    public UserProfileService()
    {
        initializeServices();
    }

    public void initializeServices()
    {
       // userRepository = new UserRepositoryImpl();
      ///  userService = new UserServiceImpl();
       // userLogService = new UserLogServiceImpl();
    }


}
