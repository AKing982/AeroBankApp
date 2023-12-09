package com.example.aerobankapp;

import com.example.aerobankapp.entity.UserLog;

import java.util.List;

public interface LoggedUser
{
    List<UserLog> getUserLogEntity();
    String getLoggedUser();
}
