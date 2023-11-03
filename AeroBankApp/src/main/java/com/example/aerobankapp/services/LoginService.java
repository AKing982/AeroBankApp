package com.example.aerobankapp.services;
import com.example.aerobankapp.model.RegistrationDTO;
import com.example.aerobankapp.model.User;
import org.springframework.stereotype.Service;

public interface LoginService
{
    boolean authenticateUser(String user, String pass);
    boolean registerUser(RegistrationDTO registrationRequest);
    void userPasswordReset(String email);
    void logout(String user);
    User getUserByUsername(String username);
}
