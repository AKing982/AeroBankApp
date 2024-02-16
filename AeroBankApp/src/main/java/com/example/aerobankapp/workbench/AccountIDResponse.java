package com.example.aerobankapp.workbench;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountIDResponse
{
    private int accountID;

    public AccountIDResponse(int acctID)
    {
        this.accountID = acctID;
    }
}
