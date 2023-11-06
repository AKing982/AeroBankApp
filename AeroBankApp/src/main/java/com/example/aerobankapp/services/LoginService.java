package com.example.aerobankapp.services;
import com.example.aerobankapp.model.RegistrationDTO;
import com.example.aerobankapp.model.User;
import org.springframework.stereotype.Service;

public interface LoginService
{
    void login();
    boolean registerUser(RegistrationDTO registrationRequest);
}
