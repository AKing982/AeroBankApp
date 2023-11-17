package com.example.aerobankapp.workbench.controllers.fxml;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.services.LoginThreadTaskService;
import com.example.aerobankapp.workbench.LoginGUI;
import com.example.aerobankapp.workbench.login.Login;
import com.example.aerobankapp.workbench.model.LoginModel;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Slf4j
@Component
public class LoginController
{
    private LoginModel currentLogin;
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    public LoginController(AuthenticationServiceImpl authenticationService, LoginModel login)
    {
        this.currentLogin = login;
        this.authenticationService = authenticationService;
    }

    private ThreadPoolTaskExecutor getThreadPoolExecutor()
    {
        return new ThreadPoolTaskExecutor();
    }

    private LoginThreadTaskService getLoginThreadProcess()
    {
        return new LoginThreadTaskService(getThreadPoolExecutor());
    }

    private boolean isAuthenticationValid(String user, String pass)
    {
        return getAuthenticationService().authenticate(user, pass);
    }

    private Map<String, String> getCredentialsMap()
    {
        return currentLogin.getCredentialsMap();
    }

    private Map<String, String> getUserCredentials()
    {
        return new HashMap<>(getCredentialsMap());
    }

    private String getUserFromMap()
    {
        String user = "";
        String password = getPassword();
        for(Map.Entry<String, String> entry : getCredentialsMap().entrySet())
        {
            if(password.equals(entry.getValue()))
            {
                user = entry.getKey();
                break;
            }
        }
        return user;
    }

    private String getUserName()
    {
        return currentLogin.getUsername();
    }

    private String getPassword()
    {
        return currentLogin.getPassword();
    }

    public void getLogin()
    {
        Map<String, String> credentials = getUserCredentials();
        String password = credentials.get(getPassword());
        String user = getUserFromMap();

        boolean isAuthenticated = isAuthenticationValid(user, password);
        if(isAuthenticated)
        {
            LoginThreadTaskService loginThreadTaskService = getLoginThreadProcess();
            // TODO: Start the Login Thread Process

            // TODO: Start Thread process for creating a User Session Token

            // TODO: Launch UserLogServiceTask Runnable

            // TODO: Launch Home

        }
    }



}

