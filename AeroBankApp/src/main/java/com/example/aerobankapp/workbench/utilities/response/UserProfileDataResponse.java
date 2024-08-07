package com.example.aerobankapp.workbench.utilities.response;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserProfileDataResponse
{
    private String name;
    private String email;
    private String lastLogin;

    public UserProfileDataResponse(String name, String email, String lastLogin) {
        this.name = name;
        this.email = email;
        this.lastLogin = lastLogin;
    }
}
