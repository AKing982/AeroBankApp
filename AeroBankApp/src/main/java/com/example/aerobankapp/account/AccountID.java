package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * This class will manage the AccountID and build the AccountID
 * based off the User's name and AccountType
 * @author alexking
 */
@Getter
@Setter
public class AccountID
{
    private AccountType accountType;
    private final String name;
    private String accountID;

    /**
     * This constructor is to be used when AccountType and Fullname or FirstName are provided
     * @param acctType
     * @param fullName
     */
    public AccountID(AccountType acctType, String fullName)
    {
        nullCheck(acctType);
        this.name = fullName;
    }

    /**
     * This method is to be used when only providing the user's full name
     * Using this method will require using the getAccountID method to build the AccountID
     * @param fullName
     */
    public AccountID(String fullName)
    {
        this.name = fullName;
    }

    private void nullCheck(AccountType accountType)
    {
        if(accountType == null)
        {
            throw new IllegalArgumentException("Invalid Account Type argument: " + accountType);
        }
        this.accountType = accountType;
    }

    private String getFirstNamePrefix()
    {
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Invalid First Name found: " + name);
        }
        return name.trim().substring(0, 1);
    }

    public String buildID()
    {
        int numPrefix;
        switch(accountType)
        {
            case CHECKING -> {
                numPrefix = 1;
            }
            case SAVINGS -> {
                numPrefix = 2;
            }
            case RENT -> {
                numPrefix = 3;
            }
            case MORTGAGE ->
            {
                numPrefix = 4;
            }
            case INVESTMENT ->
            {
                numPrefix = 5;
            }
            default -> {
                throw new IllegalArgumentException("Invalid Account Type: " + accountType);
            }
        }
        return getFirstNamePrefix() + numPrefix;

    }

    public String getAccountID(final AccountType accountType)
    {
        this.accountType = accountType;
        return buildID();
    }

}
