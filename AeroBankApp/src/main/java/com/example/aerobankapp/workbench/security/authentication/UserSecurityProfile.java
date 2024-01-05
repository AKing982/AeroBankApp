package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Component
public class UserSecurityProfile implements Cloneable
{
    private AccountStatus accountStatus;
    private UserStatus userStatus;
    private TransactionSecurity transactionStatus;
    private SchedulingSecurity schedulingStatus;
    private ApprovalStatus approvalStatus;
    private SecurityUser securityUser;
    private Set<AccountStatus> accountStatusSet;
    private Set<TransactionSecurity> transactionSecuritySet;
    private Set<SchedulingSecurity> schedulingSecuritySet;

    private BankAuthorization bankAuthorization;

    @Autowired
    public UserSecurityProfile()
    {

    }


    /**
     * This method will collect the authority of the user based on the user's role and Granted Authority
     * @param role
     * @return
     */
    public SecurityDataModel collectAuthorities(UserType role)
    {
        // Collect the Authorization
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        GrantedAuthority authority = setGrantedAuthority(role.toString());
        grantedAuthorities.add(authority);

        // Collect the Security profile based on the Authorization
       // UserSecurityProfile securityProfile = getCurrentSecurityProfile(role);

        // Save the Authorization Rules to the SecurityDataModel
        SecurityDataModel securityDataModel = new SecurityDataModel();
     //   securityDataModel.setUserSecurityProfile(securityProfile);
        securityDataModel.setGrantedAuthorities(grantedAuthorities);

        return securityDataModel;
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
