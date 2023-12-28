package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * This class will manage the AccountID
 * @author alexking
 */
@Getter
@Setter
public class AccountID
{
    private String accountID;
    private GenerateAccountID generateAccountID;

    public AccountID(AccountType accountType, String fullName)
    {
        this.generateAccountID = new GenerateAccountID(accountType, fullName);
        this.accountID = generateAccountID.buildID();
    }

    public String getAccountID()
    {
        return accountID;
    }

}
