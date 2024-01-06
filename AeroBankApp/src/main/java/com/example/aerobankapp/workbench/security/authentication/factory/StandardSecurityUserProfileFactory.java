package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import org.springframework.stereotype.Component;

@Component
public class StandardSecurityUserProfileFactory implements AbstractUserSecurityProfileFactory
{
    @Override
    public UserSecurityProfile createAuthority()
    {
        return null;
    }
}
