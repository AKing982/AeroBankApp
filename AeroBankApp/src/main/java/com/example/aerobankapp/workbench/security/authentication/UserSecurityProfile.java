package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserSecurityProfile implements Cloneable {

    private SecurityUser securityUser;
    private RoleService role;

    private Set<AccountStatus> accountStatusSet;
    private Set<TransactionSecurity> transactionSecuritySet;
    private Set<SchedulingSecurity> schedulingSecuritySet;
    private Set<UserSecurityModelImpl> userStatusSet;
    private UserSecurityModelImpl userSecurity;

    private UserProfileFacade userProfileFacade;


    public UserSecurityProfile(RoleService role, User user) {
        Objects.requireNonNull(role, "Role cannot but null");
        this.role = role;
    }

    public UserSecurityProfile getUserSecurityProfileFromFactory() {
        String user = getSecurityUser().getUsername();
        Role bankRole = getRole().getRoleByUserName(user);
        return new UserSecurityProfileProducer().getSecurityProfileFactory(bankRole);
    }

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    public RoleService getRole() {
        return role;
    }

    public void setRole(RoleService role) {
        this.role = role;
    }

    public Set<AccountStatus> getAccountStatusSet() {
        return accountStatusSet;
    }

    public void setAccountStatusSet(Set<AccountStatus> accountStatusSet) {
        this.accountStatusSet = accountStatusSet;
    }

    public Set<TransactionSecurity> getTransactionSecuritySet() {
        return transactionSecuritySet;
    }

    public void setTransactionSecuritySet(Set<TransactionSecurity> transactionSecuritySet) {
        this.transactionSecuritySet = transactionSecuritySet;
    }

    public Set<SchedulingSecurity> getSchedulingSecuritySet() {
        return schedulingSecuritySet;
    }

    public void setSchedulingSecuritySet(Set<SchedulingSecurity> schedulingSecuritySet) {
        this.schedulingSecuritySet = schedulingSecuritySet;
    }

    public Set<UserSecurityModelImpl> getUserStatusSet() {
        return userStatusSet;
    }

    public void setUserStatusSet(Set<UserSecurityModelImpl> userStatusSet) {
        this.userStatusSet = userStatusSet;
    }

    public UserSecurityModelImpl getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurityModelImpl userSecurity) {
        this.userSecurity = userSecurity;
    }

    public UserProfileFacade getUserProfileFacade() {
        return userProfileFacade;
    }

    public void setUserProfileFacade(UserProfileFacade userProfileFacade) {
        this.userProfileFacade = userProfileFacade;
    }

    public UserSecurityProfile getUserSecurityProfile(Role role)
    {
        if(role == null)
        {
            throw new NullPointerException("Null Role Found");
        }
        return new UserSecurityProfileProducer().getSecurityProfileFactory(role);
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
