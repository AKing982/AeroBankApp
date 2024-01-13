package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;

/**
 * This class will manage the AccountID
 * @author alexking
 */
@Getter
@Setter
public class AccountCode
{
    private String accountID;
    private GenerateAccountCode generateAccountID;

    public AccountCode(AccountType accountType, String fullName)
    {
        this.generateAccountID = new GenerateAccountCode(accountType, fullName);
        this.accountID = generateAccountID.buildID();
    }


}
