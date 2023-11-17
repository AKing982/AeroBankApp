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
    private List<Runnable> loginTasks;
    private List<Runnable> depositTasks;
    private List<Runnable> registerTasks;

    @Autowired
    public LoginThreadTaskService(@Qualifier("taskExecutor") TaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }

    private void runTasks()
    {
        if(threadType == ThreadType.LOGIN)
        {
            for(Runnable e : loginTasks)
            {
                taskExecutor.execute(e);
            }
        }
        else if(threadType == ThreadType.REGISTER)
        {
            for(Runnable e : registerTasks)
            {
                taskExecutor.execute(e);
            }
        }

    }




}
