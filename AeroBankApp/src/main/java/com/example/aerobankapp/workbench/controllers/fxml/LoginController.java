package com.example.aerobankapp.workbench.controllers.fxml;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.services.LoginThreadTaskService;
import com.example.aerobankapp.workbench.LoginGUI;
import com.example.aerobankapp.workbench.model.Login;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
@Slf4j
public class LoginController
{
    private Login currLogin;
    private LoginGUI loginGUI;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    public LoginController(LoginGUI loginGUI, Login login)
    {
        this.currLogin = login;
        this.loginGUI = loginGUI;
    }

    public void getLogin()
    {
        String user = getCurrLogin().getUsername();
        String password = getCurrLogin().getPassword();
        boolean isAuthenticated = getAuthenticationService().authenticate(user, password);
        if(isAuthenticated)
        {
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            LoginThreadTaskService loginThreadTaskService = new LoginThreadTaskService(taskExecutor);
            // TODO: Start the Login Thread Process

            // TODO: Start Thread process for creating a User Session Token

            // TODO: Launch UserLogServiceTask Runnable

            // TODO: Launch Home


        }
        else
        {
            getLoginGUI().getLoginAlert().setText("Invalid Username or password");
        }
    }



}

