package com.example.aerobankapp.workbench;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCodeIDResponse {
    private int acctCodeID;

    public AccountCodeIDResponse(int acctCodeID){
        this.acctCodeID = acctCodeID;
    }
}
