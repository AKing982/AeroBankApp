package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.factory.schedulerSecurity.SchedulerSecurity;
import com.example.aerobankapp.workbench.utilities.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Getter
@Component
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserSecurityProfile implements Cloneable {
    private AccountStatus accountStatus;
    private UserStatus userStatus;
    private TransactionSecurity transactionStatus;
    private SchedulingSecurity schedulingStatus;
    private ApprovalStatus approvalStatus;
    private SecurityUser securityUser;
    private Set<AccountStatus> accountStatusSet;
    private Set<UserStatus> userStatusSet;
    private Set<TransactionSecurity> transactionSecuritySet;
    private Set<SchedulingSecurity> schedulingSecuritySet;
    private Set<ApprovalStatus> approvalStatusSet;
    private UserSecurityProfileProducer userSecurityProfileProducer;
    private Role role;

    @Autowired
    public UserSecurityProfile(Role bankRole) {
        Objects.requireNonNull(bankRole, "Role cannot but null");
        this.role = bankRole;
    }

    public UserSecurityProfile()
    {

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
        schedulingSecuritySet.add(schedulingSecurity);
    }

    public void setTransactionSecurityPermissions(TransactionSecurity transactionStatus) {
        transactionSecuritySet.add(transactionStatus);
    }

    public void setAccountSecurityPermissions(AccountStatus accountStatus)
    {
        accountStatusSet.add(accountStatus);
    }

    public void setUserPermissions(UserStatus userStatus)
    {
        userStatusSet.add(userStatus);
    }

    public void setApprovalPermissions(ApprovalStatus approvalStatus)
    {
        approvalStatusSet.add(approvalStatus);
    }

    public UserStatus getUserPermissionFromSet(UserStatus permission)
    {
        return userStatusSet.stream().filter(e -> e.equals(permission)).findFirst().orElseThrow();
    }

    public AccountStatus getAccountPermissionFromSet(AccountStatus accountStatus)
    {
        return accountStatusSet.stream().filter(e -> e.equals(accountStatus)).findFirst().orElseThrow();
    }

    public TransactionSecurity getTransactionPermissionFromSet(TransactionSecurity transactionStatus)
    {
        return transactionSecuritySet.stream().filter(e -> e.equals(transactionStatus)).findFirst().orElseThrow();
    }

    public SchedulingSecurity getSchedulerPermissionFromSet(SchedulingSecurity schedulingSecurity)
    {
        return schedulingSecuritySet.stream().filter(e -> e.equals(schedulingSecurity)).findFirst().orElseThrow();
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
