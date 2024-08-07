package com.example.aerobankapp.workbench.utilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountNameResponse
{
    private String accountName;

    public AccountNameResponse(String accountName){
        this.accountName = accountName;
    }
}
