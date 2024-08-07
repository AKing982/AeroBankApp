package com.example.aerobankapp.workbench.tokens;

import com.example.aerobankapp.controllers.AuthController;
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
    private String username;

    public AuthTokenResponse(String token, String type)
    {
        this.token = token;
        this.type = type;
    }

    public AuthTokenResponse(String token, String type, String user)
    {
        this.token = token;
        this.type = type;
        this.username = user;
    }



}
