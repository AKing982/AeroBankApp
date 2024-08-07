package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.services.UserProfileService;
import lombok.Getter;
import lombok.Setter;


public class UserProfileFacade
{
    private final UserProfileService userProfileService;


    public UserProfileFacade(UserProfileService userProfileService)
    {
        this.userProfileService = userProfileService;
    }

    public UserProfileService getUserProfileService() {
        return userProfileService;
    }
}
