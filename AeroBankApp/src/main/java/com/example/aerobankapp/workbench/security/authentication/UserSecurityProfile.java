package com.example.aerobankapp.workbench.security.authentication;

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

    private SecurityUser securityUser;
    private Role role;

    private Set<AccountStatus> accountStatusSet;
    private Set<TransactionSecurity> transactionSecuritySet;
    private Set<SchedulingSecurity> schedulingSecuritySet;
    private Set<UserStatus> userStatusSet;
    private UserSecurityModelImpl userSecurity;
    private UserProfile userProfile;


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
