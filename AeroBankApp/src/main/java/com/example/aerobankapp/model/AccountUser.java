package com.example.aerobankapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUser
{
    private int acctID;
    private int userID;

    public AccountUser(int acctID, int userID){
        this.acctID = acctID;
        this.userID = userID;
    }
}
