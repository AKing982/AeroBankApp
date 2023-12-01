package com.example.aerobankapp.workbench.tokens;

import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenManager {
    private static TokenManager instance;
    private JWTUtil jwtUtil;
    private Map<String, User> sessionTokens;


    private TokenManager() {
        sessionTokens = new HashMap<>();
        jwtUtil = new JWTUtil();
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public String generateUserToken(User user) {
        String username = user.getUser();
        String token = jwtUtil.generateToken(username);
        sessionTokens.put(token, user);
        return token;
    }

    private User getUserFromToken(String token)
    {
        return sessionTokens.get(token);
    }
}