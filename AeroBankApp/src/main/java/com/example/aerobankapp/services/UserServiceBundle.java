package com.example.aerobankapp.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Deprecated
public class UserServiceBundle
{
    private UserLogDAOImpl userLogService;
    private UserDAOImpl userService;

    @Autowired
    public UserServiceBundle(UserLogDAOImpl userLogService, UserDAOImpl service)
    {
        this.userLogService = userLogService;
        this.userService = service;
    }
}
