package com.example.aerobankapp.account;

public class AccountID
{
    private AccountType accountType;

    public AccountID(AccountType acctType)
    {
        this.accountType = acctType;
    }

    public String buildID()
    {
        String acctID = "";
        switch(accountType)
        {
            case CHECKING:
                acctID = getCheckingAccountID();
                break;

        }
        return acctID;
    }

    public String getCheckingAccountID()
    {
        return "";
    }
}
