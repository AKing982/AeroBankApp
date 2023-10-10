package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.User;
import org.springframework.stereotype.Component;

public class UserProfile extends AbstractUserProfile
{
    private LoggedUser loggedUser;
    private User user;

    public UserProfile(String name)
    {
        super(name);
    }

}
