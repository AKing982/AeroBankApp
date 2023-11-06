package com.example.aerobankapp.workbench.tokens;

import com.example.aerobankapp.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManager
{
    private static TokenManager instance;
    private Map<String, User> sessionTokens;

    private TokenManager()
    {
        sessionTokens = new HashMap<>();
    }

    public static TokenManager getInstance()
    {
        if(instance == null)
        {
            instance = new TokenManager();
        }
        return instance;
    }

    public String generateUserToken(User user)
    {
        String token = UUID.randomUUID().toString();
        sessionTokens.put(token, user);
        return token;
    }

    private User getUserFromToken(String token)
    {
        return sessionTokens.get(token);
    }
}
