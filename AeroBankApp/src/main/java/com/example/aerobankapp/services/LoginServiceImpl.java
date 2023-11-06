package com.example.aerobankapp.services;

import com.example.aerobankapp.model.RegistrationDTO;
import com.example.aerobankapp.repositories.UserRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl
{
    private UserRepository repository;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository)
    {
        this.repository = userRepository;
    }


}
