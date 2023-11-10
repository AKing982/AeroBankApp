package com.example.aerobankapp.services;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
public class AccountServiceBundle
{
    private CheckingRepositoryServiceImpl checkingService;
    private SavingsAccountServiceImpl savingsService;

    @Autowired
    public AccountServiceBundle(CheckingRepositoryServiceImpl checking, SavingsAccountServiceImpl savings)
    {
        this.checkingService = checking;
        this.savingsService = savings;
    }
}
