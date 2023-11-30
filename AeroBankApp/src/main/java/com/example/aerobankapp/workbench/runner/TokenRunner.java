package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.tokens.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
public class TokenRunner implements Callable<String>
{
    private TokenManager tokenManager;

    @Autowired
    public TokenRunner(TokenManager tokenManager)
    {
        this.tokenManager = tokenManager;
    }

    @Override
    public String call() throws Exception
    {

    }
}
