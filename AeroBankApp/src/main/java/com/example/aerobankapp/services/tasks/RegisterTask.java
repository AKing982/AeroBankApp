package com.example.aerobankapp.services.tasks;

import com.example.aerobankapp.services.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterTask implements Task
{
    private List<Task> tasks;

    @Override
    public void execute()
    {

    }
}
