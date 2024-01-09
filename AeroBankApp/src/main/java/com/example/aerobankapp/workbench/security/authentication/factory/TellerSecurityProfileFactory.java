package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.AccountStatus;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.SchedulingSecurity;
import com.example.aerobankapp.workbench.utilities.TransactionSecurity;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class TellerSecurityProfileFactory implements AbstractUserSecurityProfileFactory
{

    @Override
    public UserSecurityProfile createAuthority()
    {
        return null;
    }
}
