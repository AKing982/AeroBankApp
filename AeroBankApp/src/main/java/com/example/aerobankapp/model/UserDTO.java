package com.example.aerobankapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
public class UserDTO
{
    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private char[] password;
    private int pinNumber;
    private boolean isAdmin;

    public UserDTO()
    {

    }
}
