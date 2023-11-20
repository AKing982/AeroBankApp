package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Getter
public class UserLogRunner implements Runnable
{
    private final UserLogServiceImpl userLogService;
    private final UserServiceImpl userService;
    private final LoginModel loginModel;

    @Autowired
    public UserLogRunner(UserLogServiceImpl userLogService, UserServiceImpl userSvc, LoginModel loginModel)
    {
        this.userLogService = userLogService;
        this.userService = userSvc;
        this.loginModel = loginModel;
    }

    private String getUserName()
    {
        return getLoginModel().getUsername();
    }

    public int getCurrentUserID()
    {

        List<Users> users = getUserService().findByUserName(getUserName());
        return users.stream()
                .filter(user -> user.getUsername().equals(getUserName()))
                .findFirst()
                .map(Users::getId)
                .orElse(null);
        
    }

    @Override
    public void run()
    {

    }
}
