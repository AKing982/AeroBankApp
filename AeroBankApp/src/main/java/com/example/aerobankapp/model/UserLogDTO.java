package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserLogDTO
{
    private int id;
    private int userID;
    private String username;
    private Date lastLogin;

    public UserLogDTO(int userID, String username, Date lastLogin)
    {
        this.userID = userID;
        this.username = username;
        this.lastLogin = lastLogin;
    }
}
