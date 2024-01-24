package com.example.aerobankapp.workbench.tokens;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthTokenResponse
{
    private String token;
    private String type;

    public AuthTokenResponse(String token, String type)
    {
        this.token = token;
        this.type = type;
    }



}
