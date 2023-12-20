package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User implements Serializable
{
    private static long serialVersionUID = 1L;

    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private String password;
    private int pinNumber;
    private UserAuthority userAuthority;
}
