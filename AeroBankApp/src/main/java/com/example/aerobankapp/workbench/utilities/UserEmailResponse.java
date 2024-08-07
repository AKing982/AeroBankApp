package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailResponse
{
    private String email;

    public UserEmailResponse(String email){
        this.email = email;
    }
}
