package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.*;

import java.util.List;

public class UserProfile
{
    private LoggedUser loggedUser;
    private Users user;
    private String username;
    protected List<CheckingAccount> checkingAccounts;
    protected List<SavingsAccount> savingsAccounts;
    protected List<InvestmentAccount> investmentAccounts;
    protected List<RentAccount> rentAccounts;

    public UserProfile(String name)
    {
        this.username = name;
    }

}
