package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.entity.InvestmentAccount;
import com.example.aerobankapp.entity.RentAccount;
import com.example.aerobankapp.entity.SavingsAccount;

import java.util.List;

public abstract class AbstractUserProfile
{
    protected String username;
    protected List<CheckingAccount> checkingAccounts;
    protected List<SavingsAccount> savingsAccounts;
    protected List<InvestmentAccount> investmentAccounts;
    protected List<RentAccount> rentAccounts;

    public AbstractUserProfile(String user)
    {
        this.username = user;
    }

}
