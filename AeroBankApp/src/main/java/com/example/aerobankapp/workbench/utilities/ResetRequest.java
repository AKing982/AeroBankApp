package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResetRequest implements Serializable
{
    private String user;
    private String password;

    public ResetRequest(String username, String pass){
        this.user = username;
        this.password = pass;
    }

    public ResetRequest(){

    }
}
