package com.example.aerobankapp.workbench.threadServices;

import com.example.aerobankapp.services.LoginThreadTaskService;
import com.example.aerobankapp.services.UserDAOImpl;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl
{
    private final AeroLogger aeroLogger = new AeroLogger(LoginServiceImpl.class);
    private LoginThreadTaskService loginThreadTaskService;
    private JWTUtil tokenAuth;
    private final UserDAOImpl userRepo;

    @Autowired
    public LoginServiceImpl(UserDAOImpl userRepository)
    {
        this.userRepo = userRepository;
        tokenAuth = new JWTUtil();
    }




}
