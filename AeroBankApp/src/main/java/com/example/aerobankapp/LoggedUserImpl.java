package com.example.aerobankapp;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.services.UserLogServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@Slf4j
public class LoggedUserImpl implements LoggedUser
{
    private String currentUser;
    private final UserLogServiceImpl userLogService;

    @Autowired
    public LoggedUserImpl(UserLogServiceImpl userLogService)
    {
        this.userLogService = userLogService;
    }

    @Override
    public List<UserLog> getUserLogEntity()
    {
        return getUserLogService().findAll();
    }

    @Override
    public String getLoggedUser()
    {
        List<UserLog> userLogEntities = getUserLogEntity();
        return userLogEntities.get(0).getUsername();
    }
}
