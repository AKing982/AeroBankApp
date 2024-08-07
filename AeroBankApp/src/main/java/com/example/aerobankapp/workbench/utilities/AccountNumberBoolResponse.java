package com.example.aerobankapp.workbench.utilities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountNumberBoolResponse
{
    private boolean exists;

    public AccountNumberBoolResponse(boolean exists){
        this.exists = exists;
    }
}
