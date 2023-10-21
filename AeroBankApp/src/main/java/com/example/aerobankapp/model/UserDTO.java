package com.example.aerobankapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable
{
    private static long serialVersionUID = 1L;

    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private char[] password;
    private int pinNumber;
    private boolean isAdmin;
}
