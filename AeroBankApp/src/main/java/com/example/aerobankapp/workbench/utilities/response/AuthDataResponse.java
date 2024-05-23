package com.example.aerobankapp.workbench.utilities.response;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthDataResponse
{
    private String token;
    private String tokenType;
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public AuthDataResponse(String token, String tokenType, String username, Collection<? extends GrantedAuthority> roles){
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.roles = roles;
    }
}
