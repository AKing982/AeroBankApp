package com.example.aerobankapp.workbench.threadServices;

import com.example.aerobankapp.services.LoginService;
import com.example.aerobankapp.services.LoginThreadTaskService;
import com.example.aerobankapp.services.UserDAOService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService
{
    private final AeroLogger aeroLogger = new AeroLogger(LoginServiceImpl.class);
    private LoginThreadTaskService loginThreadTaskService;
    private JWTUtil tokenAuth;
    private final UserDAOService userRepo;

    @Autowired
    public LoginServiceImpl(UserServiceImpl userRepository)
    {
        this.userRepo = userRepository;
        tokenAuth = new JWTUtil();
    }

    @Override
    public boolean authenticateUser(String user, String pass)
    {
        String userToken = tokenAuth.generateToken(user);

        // Validate the token
        boolean isValidToken = tokenAuth.validateToken(userToken);

        if(isValidToken)
        {
            return false;
        }
        return true;

    }

    @Override
    public void login()
    {

    }


}
