package com.example.aerobankapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class LoginThreadTaskService
{
    private final TaskExecutor taskExecutor;

    @Autowired
    public LoginThreadTaskService(@Qualifier("taskExecutor") TaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }



}
