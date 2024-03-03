package com.example.aerobankapp;

import com.example.aerobankapp.workbench.runner.DatabaseRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializerImpl implements DatabaseInitializer
{
    @Override
    public void initialize(String[] args) {
       // DatabaseRunner.main(args);
    }
}
