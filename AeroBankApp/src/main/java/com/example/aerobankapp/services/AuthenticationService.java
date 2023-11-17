package com.example.aerobankapp.services;

public interface AuthenticationService
{
    boolean authenticateByUserCount(String user, String pass);

    boolean authenticate(String user, String pass);
}
