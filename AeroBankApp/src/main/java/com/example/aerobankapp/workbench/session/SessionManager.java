package com.example.aerobankapp.workbench.session;

import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.tokens.TokenManager;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Slf4j
public class SessionManager
{
    private static SessionManager instance;
    private TokenManager tokenManager;
    private User currentUser;
    private UserProfile currentProfile;

    private SessionManager()
    {

    }

    public static SessionManager getInstance()
    {
        if(instance == null)
        {
            instance = new SessionManager();
        }
        return instance;
    }

    private String getTokenFromManager()
    {
        return tokenManager.generateUserToken(currentUser);
    }

}
