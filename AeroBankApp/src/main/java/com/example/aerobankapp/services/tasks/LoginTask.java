package com.example.aerobankapp.services.tasks;

import com.example.aerobankapp.services.Task;
import com.example.aerobankapp.services.ThreadType;
import com.example.aerobankapp.services.UserDAOImpl;
import com.example.aerobankapp.services.UserLogDAOImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import com.example.aerobankapp.workbench.runner.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginTask implements Task
{

    private ThreadType threadType;
    private AuthorizationRunner authorizationTask;
    private AuthenticationRunner authenticationTask;
    private UserLogRunner userLogRunner;
    private TokenRunner tokenRunner;
    private UserProfileRunner userProfileRunner;
    private SessionRunner sessionRunner;
    private HomeRunner homeRunner;
    private boolean isAuthorizationRequired;
    private boolean isAuthenticationRequired;
    private boolean isUserLogRequired;
    private boolean isTokenRequired;
    private boolean isSessionRequired;
    private List<Task> tasks = new ArrayList<>();

    @Autowired
    private UserLogDAOImpl userLogService;

    @Autowired
    private UserDAOImpl userService;

    @Autowired
    private LoginModel loginModel;

    public void initialize()
    {
        authorizationTask = new AuthorizationRunner();
        authenticationTask = new AuthenticationRunner();
        userLogRunner = new UserLogRunner(userLogService, userService, loginModel);
    }

    @Override
    public void execute()
    {
        System.out.println("Hello");
    }

}
