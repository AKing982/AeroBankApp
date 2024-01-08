package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Objects;

@Getter
@Component
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserSecurityProfile implements Cloneable {
    private SecurityUser securityUser;
    private EnumSet<AccountStatus> accountStatusEnumSet;
    private EnumSet<UserStatus> userStatusEnumSet;
    private EnumSet<TransactionSecurity> transactionSecurityEnumSet;
    private EnumSet<SchedulingSecurity> schedulingSecurityEnumSet;
    private EnumSet<ApprovalStatus> approvalStatusEnumSet;
    private UserSecurityProfileProducer userSecurityProfileProducer;
    private Role role;

    @Autowired
    public UserSecurityProfile(Role bankRole) {
        Objects.requireNonNull(bankRole, "Role cannot but null");
        this.role = bankRole;
    }


    public UserSecurityProfile getUserSecurityProfileFromFactory() {
        return new UserSecurityProfileProducer().getSecurityProfileFactory(role);
    }

    public UserSecurityProfile getUserSecurityProfile(Role role)
    {
        if(role == null)
        {
            throw new NullPointerException("Null Role Found");
        }
        return new UserSecurityProfileProducer().getSecurityProfileFactory(role);
    }

    public void setSchedulingSecurityPermissions(SchedulingSecurity schedulingSecurity) {
        schedulingSecurityEnumSet.add(schedulingSecurity);
    }

    public void setTransactionSecurityPermissions(TransactionSecurity transactionStatus) {
        transactionSecurityEnumSet.add(transactionStatus);
    }

    public void setAccountSecurityPermissions(AccountStatus accountStatus)
    {
        accountStatusEnumSet.add(accountStatus);
    }

    public void setUserPermissions(UserStatus userStatus)
    {
        userStatusEnumSet.add(userStatus);
    }

    public void setApprovalPermissions(ApprovalStatus approvalStatus)
    {
        approvalStatusEnumSet.add(approvalStatus);
    }

    public UserStatus getUserPermissionFromSet(UserStatus permission)
    {
        return userStatusEnumSet.stream().filter(e -> e.equals(permission)).findFirst().orElseThrow();
    }

    public AccountStatus getAccountPermissionFromSet(AccountStatus accountStatus)
    {
        return accountStatusEnumSet.stream().filter(e -> e.equals(accountStatus)).findFirst().orElseThrow();
    }

    public TransactionSecurity getTransactionPermissionFromSet(TransactionSecurity transactionStatus)
    {
        return transactionSecurityEnumSet.stream().filter(e -> e.equals(transactionStatus)).findFirst().orElseThrow();
    }

    public SchedulingSecurity getSchedulerPermissionFromSet(SchedulingSecurity schedulingSecurity)
    {
        return schedulingSecurityEnumSet.stream().filter(e -> e.equals(schedulingSecurity)).findFirst().orElseThrow();
    }



    @Override
    public UserSecurityProfile clone() {
        try {
            UserSecurityProfile clone = (UserSecurityProfile) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
