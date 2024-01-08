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
        return UserSecurityProfile.builder()
                .schedulingSecurityEnumSet(EnumSet.of(SchedulingSecurity.SCHEDULING_ALLOWED))
                .accountStatusEnumSet(EnumSet.of(AccountStatus.EXPIRED))
                .accountStatusEnumSet(EnumSet.of(AccountStatus.DISABLED))
                .accountStatusEnumSet(EnumSet.of(AccountStatus.LOCKED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.PURCHASE_DISABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.WITHDRAW_DISABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.DEPOSIT_DISABLED))
                .transactionSecurityEnumSet(EnumSet.of(TransactionSecurity.TRANSFER_DISABLED))
                .role(Role.TELLER)
                .build();
    }
}
