package com.example.aerobankapp.workbench.utilities.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCodeResponse
{
    private String accountCode;

    public AccountCodeResponse(String acctCode)
    {
        this.accountCode = acctCode;
    }
}
