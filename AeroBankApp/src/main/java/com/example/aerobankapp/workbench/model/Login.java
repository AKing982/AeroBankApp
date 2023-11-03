package com.example.aerobankapp.workbench.model;

import lombok.*;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Login
{
    private String username;
    private String password;
    private String encryptedUsername;
    private char[] encryptedPassword;

    public Login(String user, String pass)
    {
        this.username = user;
        this.password = pass;
    }


}
