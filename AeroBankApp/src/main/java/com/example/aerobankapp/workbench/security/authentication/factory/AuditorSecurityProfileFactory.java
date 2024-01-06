package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.AccountStatus;
import com.example.aerobankapp.workbench.utilities.TransactionSecurity;
import org.springframework.stereotype.Component;

@Component
public class AuditorSecurityProfileFactory implements AbstractUserSecurityProfileFactory
{
    public AuditorSecurityProfileFactory()
    {

    }

    @Override
    public UserSecurityProfile createAuthority()
    {
        return null;
    }
}
