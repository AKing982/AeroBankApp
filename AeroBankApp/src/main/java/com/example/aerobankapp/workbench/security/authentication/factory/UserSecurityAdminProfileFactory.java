package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.*;

public class UserSecurityAdminProfileFactory implements AbstractUserSecurityProfileFactory
{

    @Override
    public UserSecurityProfile createAuthority()
    {
        return new UserSecurityProfile.UserSecurityProfileBuilder()
                .bankAuthorization(BankAuthorization.ADMIN)
                .userStatus(UserStatus.IS_ADMIN)
                .accountStatus(AccountStatus.ENABLED)
                .accountStatus(AccountStatus.NON_EXPIRED)
                .transactionStatus(TransactionSecurity.DEPOSIT_ENABLED)
                .transactionStatus(TransactionSecurity.PURCHASE_ENABLED)
                .transactionStatus(TransactionSecurity.TRANSFER_ENABLED)
                .schedulingStatus(SchedulingSecurity.SCHEDULING_ALLOWED)
                .build();
    }
}
