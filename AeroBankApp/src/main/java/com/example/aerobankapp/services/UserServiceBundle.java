package com.example.aerobankapp.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserServiceBundle
{
    private UserLogServiceImpl userLogService;
    private UserServiceImpl userService;

    @Autowired
    public UserServiceBundle(UserLogServiceImpl userLogService, UserServiceImpl service)
    {
        this.userLogService = userLogService;
        this.userService = service;
    }
}
