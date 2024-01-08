package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.*;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class UserSecurityAdminProfileFactory implements AbstractUserSecurityProfileFactory
{

    @Override
    public UserSecurityProfile createAuthority()
    {

        return UserSecurityProfile.builder()
                .role(Role.ADMIN)
                .userStatusEnumSet(EnumSet.of(UserStatus.IS_ADMIN))
                .accountStatusEnumSet(EnumSet.of(AccountStatus.ENABLED))
                .schedulingSecurityEnumSet(EnumSet.of(SchedulingSecurity.SCHEDULING_ALLOWED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.PURCHASE_ENABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.DEPOSIT_ENABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.TRANSFER_ENABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.WITHDRAW_ENABLED))
                .build();
    }
}
