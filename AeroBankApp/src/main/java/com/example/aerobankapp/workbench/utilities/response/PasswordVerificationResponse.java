package com.example.aerobankapp.workbench.utilities.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordVerificationResponse
{
    private boolean matches;

    public PasswordVerificationResponse(boolean isMatch){
        this.matches = isMatch;
    }

}
