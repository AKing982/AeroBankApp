package com.example.aerobankapp.services;

import com.example.aerobankapp.services.tasks.LoginTask;
import com.example.aerobankapp.services.tasks.RegisterTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
public class LoginThreadTaskService
{
    private final TaskExecutor taskExecutor;
    private ThreadType threadType;
    private RegisterTask registerTask;
    private LoginTask loginTask;

    @Autowired
    public LoginThreadTaskService(@Qualifier("taskExecutor") TaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }

    private void runTasks(List<Task> tasks)
    {
        for(Task task : tasks)
        {
            taskExecutor.execute((Runnable) task);
        }
    }

    private void loginTasks()
    {
        List<Task> loginTasks = getLoginTaskList();
        runTasks(loginTasks);
    }

    private List<Task> getLoginTaskList()
    {
        return loginTask.getTasks();
    }

    private List<Task> getRegisterTaskList()
    {
        return new ArrayList<>();
    }


    private void registerTasks()
    {
        List<Task> registerTasks = getRegisterTaskList();
       runTasks(registerTasks);
    }

}
