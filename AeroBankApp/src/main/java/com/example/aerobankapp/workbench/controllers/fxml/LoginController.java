package com.example.aerobankapp.workbench.controllers.fxml;

import com.example.aerobankapp.model.RegistrationDTO;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.LoginGUI;
import com.example.aerobankapp.workbench.model.Login;
import com.example.aerobankapp.workbench.threadServices.LoginService;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class LoginController
{
    private Login currLogin;
    private AuthenticationManager authenticationManager;
    private final RegistrationService regService;
    private final LoginService loginService;
    private final AeroLogger aeroLogger = new AeroLogger(LoginController.class);

    @Autowired
    public LoginController(Login login)
    {
        if(login != null)
        {
            this.currLogin = login;
        }
    }

}

