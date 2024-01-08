package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.entity.InvestmentAccountEntity;
import com.example.aerobankapp.entity.RentAccountEntity;
import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.workbench.transactions.CardDesignator;

import java.util.Set;

public interface UserSecurityModel
{
    Set<CheckingAccountEntity> getCheckingAccountDetails();
    Set<SavingsAccountEntity> getSavingsAccountDetails();
    Set<InvestmentAccountEntity> getInvestmentAccountDetails();
    Set<RentAccountEntity> getRentAccountDetails();
    Set<CardDesignator> getUserCardDetails();
    boolean isUserEnabled();
}
