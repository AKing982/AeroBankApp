package com.example.aerobankapp.services;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Builder
@Getter
@Setter
public class AccountServiceBundle
{
    private CheckingAccountDAO checkingService;
    private SavingsAccountDAO savingsService;
    private InvestmentAccountDAO investmentAccountDAO;

    @Autowired
    public AccountServiceBundle(CheckingAccountDAO checking, SavingsAccountDAO savings, InvestmentAccountDAO investmentAccountDAO)
    {
        this.checkingService = checking;
        this.savingsService = savings;
        this.investmentAccountDAO = investmentAccountDAO;
    }
}
