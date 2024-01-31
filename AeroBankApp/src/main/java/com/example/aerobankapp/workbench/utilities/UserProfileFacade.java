package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileFacade
{
    private final UserProfileService userProfileService;


    public UserProfileFacade(UserProfileService userProfileService)
    {
        this.userProfileService = userProfileService;
    }
}
