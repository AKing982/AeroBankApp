package com.example.aerobankapp.workbench.tokens;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenResponse
{
    private String token;
    private String type = "Bearer";

    public AuthTokenResponse(String token)
    {
        this.token = token;
    }

}
