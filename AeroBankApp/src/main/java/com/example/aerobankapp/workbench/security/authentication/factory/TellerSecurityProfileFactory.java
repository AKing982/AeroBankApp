package com.example.aerobankapp.workbench.security.authentication.factory;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import com.example.aerobankapp.workbench.utilities.AccountStatus;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.SchedulingSecurity;
import com.example.aerobankapp.workbench.utilities.TransactionSecurity;
import org.springframework.stereotype.Component;

@Component
public class TellerSecurityProfileFactory implements AbstractUserSecurityProfileFactory
{

    @Override
    public UserSecurityProfile createAuthority()
    {
        return UserSecurityProfile.builder()
                .schedulingStatus(SchedulingSecurity.SCHEDULING_ALLOWED)
                .accountStatus(AccountStatus.EXPIRED)
                .accountStatus(AccountStatus.DISABLED)
                .accountStatus(AccountStatus.LOCKED)
                .transactionStatus(TransactionSecurity.PURCHASE_DISABLED)
                .transactionStatus(TransactionSecurity.WITHDRAW_DISABLED)
                .transactionStatus(TransactionSecurity.DEPOSIT_DISABLED)
                .transactionStatus(TransactionSecurity.TRANSFER_DISABLED)
                .role(Role.TELLER)
                .build();
    }
}
