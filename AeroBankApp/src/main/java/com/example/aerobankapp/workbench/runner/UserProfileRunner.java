package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;


public class UserProfileRunner implements Callable<UserProfile>
{

    private UserProfileService userProfileService;

    public UserProfileRunner()
    {

    }

    @Override
    public UserProfile call() throws Exception
    {
        return null;
    }
}
