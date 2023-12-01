package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Component
public class UserLogModel
{
    private int id;
    private int userID;
    private String username;
    private Date lastLogin;
    private List<UserLogModel> userLogHistory = new ArrayList<>();

    public UserLogModel(int userID, String username, Date lastLogin)
    {
        this.userID = userID;
        this.username = username;
        this.lastLogin = lastLogin;
    }

    public String getLoggedUser()
    {
        return username;
    }
}