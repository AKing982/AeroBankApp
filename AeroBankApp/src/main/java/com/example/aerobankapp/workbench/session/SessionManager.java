package com.example.aerobankapp.workbench.session;

import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.tokens.TokenManager;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.springframework.stereotype.Component;

@Component
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

    public void setCurrentUser(User user)
    {
        currentUser = user;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    private String getTokenFromManager()
    {
        return tokenManager.generateUserToken(currentUser);
    }

    public void setUserProfile(UserProfile userProfile)
    {
        this.currentProfile = userProfile;
    }

    private UserProfile getCurrentProfile()
    {
        return currentProfile;
    }
}
