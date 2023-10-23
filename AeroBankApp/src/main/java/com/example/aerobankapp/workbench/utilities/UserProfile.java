package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.Users;

public class UserProfile extends AbstractUserProfile
{
    private LoggedUser loggedUser;
    private Users user;

    public UserProfile(String name)
    {
        super(name);
    }

}
