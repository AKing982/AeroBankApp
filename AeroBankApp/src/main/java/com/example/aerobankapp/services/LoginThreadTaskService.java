package com.example.aerobankapp.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
@Getter
public class LoginThreadTaskService
{
    private final TaskExecutor taskExecutor;
    private ThreadType threadType;
    private List<Object> loginTasks;
    private List<Object> depositTasks;
    private List<Object> registerTasks;

    @Autowired
    public LoginThreadTaskService(@Qualifier("taskExecutor") TaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }

    private void runTasks()
    {

    }




}
