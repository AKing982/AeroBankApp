package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.*;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityAdminProfileFactory implements AbstractUserSecurityProfileFactory
{

    @Override
    public UserSecurityProfile createAuthority()
    {

        return UserSecurityProfile.builder()
                .role(Role.ADMIN)
                .userStatus(UserStatus.IS_ADMIN)
                .accountStatus(AccountStatus.ENABLED)
                .schedulingStatus(SchedulingSecurity.SCHEDULING_ALLOWED)
                .transactionStatus(TransactionSecurity.PURCHASE_ENABLED)
                .transactionStatus(TransactionSecurity.DEPOSIT_ENABLED)
                .transactionStatus(TransactionSecurity.TRANSFER_ENABLED)
                .transactionStatus(TransactionSecurity.WITHDRAW_ENABLED)
                .build();
    }
}
