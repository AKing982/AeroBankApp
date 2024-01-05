package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;

public interface AbstractUserSecurityProfileFactory
{
    UserSecurityProfile createAuthority();
}
