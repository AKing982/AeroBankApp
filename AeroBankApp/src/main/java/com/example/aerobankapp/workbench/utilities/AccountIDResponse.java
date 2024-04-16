package com.example.aerobankapp.workbench.utilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountIDResponse {
    private int accountID;

    public AccountIDResponse(int acctID){
        this.accountID = acctID;
    }
}
