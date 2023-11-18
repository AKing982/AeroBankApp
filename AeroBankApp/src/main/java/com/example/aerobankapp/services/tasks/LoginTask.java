package com.example.aerobankapp.services.tasks;

import com.example.aerobankapp.services.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginTask implements Task
{

    @Override
    public void execute()
    {
        System.out.println("Hello");
    }
}
