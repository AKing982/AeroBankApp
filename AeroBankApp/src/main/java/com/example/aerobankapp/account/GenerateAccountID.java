package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateAccountID
{
    private AccountType accountType;
    private String name;

    /**
     * This constructor is to be used when AccountType and Fullname or FirstName are provided
     * @param accountType
     * @param fullName
     */

    public GenerateAccountID(AccountType accountType, String fullName)
    {
        nullCheck(accountType);
        this.name = fullName;
    }

    private String getFirstNamePrefix()
    {
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Invalid First Name found: " + name);
        }
        return name.trim().substring(0, 1);
    }

    private void nullCheck(AccountType accountType)
    {
        if(accountType == null)
        {
            throw new IllegalArgumentException("Invalid Account Type argument: " + accountType);
        }
        this.accountType = accountType;
    }


    public String buildID() {
        int numPrefix;
        switch (accountType) {
            case CHECKING -> {
                numPrefix = 1;
            }
            case SAVINGS -> {
                numPrefix = 2;
            }
            case RENT -> {
                numPrefix = 3;
            }
            case MORTGAGE -> {
                numPrefix = 4;
            }
            case INVESTMENT -> {
                numPrefix = 5;
            }
            default -> {
                throw new IllegalArgumentException("Invalid Account Type: " + accountType);
            }
        }
        return getFirstNamePrefix() + numPrefix;
    }

}
