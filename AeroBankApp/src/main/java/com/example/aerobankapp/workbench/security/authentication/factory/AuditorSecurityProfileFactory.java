package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.AccountStatus;
import com.example.aerobankapp.workbench.utilities.TransactionSecurity;

public class AuditorSecurityProfileFactory implements AbstractUserSecurityProfileFactory
{
    public AuditorSecurityProfileFactory(AccountStatus accountStatus, TransactionSecurity transactionStatus,)

    @Override
    public UserSecurityProfile createAuthority()
    {
        return null;
    }
}
