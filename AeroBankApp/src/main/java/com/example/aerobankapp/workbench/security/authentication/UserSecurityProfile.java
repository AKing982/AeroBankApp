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
    private RoleService role;

    private Set<AccountStatus> accountStatusSet;
    private Set<TransactionSecurity> transactionSecuritySet;
    private Set<SchedulingSecurity> schedulingSecuritySet;
    private Set<UserSecurityModelImpl> userStatusSet;
    private UserSecurityModelImpl userSecurity;
    private UserProfile userProfile;

    @Autowired
    private UserProfileFacade userProfileFacade;

    @Autowired
    public UserSecurityProfile(RoleService role, String user) {
        Objects.requireNonNull(role, "Role cannot but null");
        this.role = role;
        this.userProfile = new UserProfile(user);
        this.userProfile.setUserProfileFacade(userProfileFacade);
    }


    public UserSecurityProfile getUserSecurityProfileFromFactory() {
        String user = getSecurityUser().getUsername();
        Role bankRole = getRole().getRoleByUserName(user);
        return new UserSecurityProfileProducer().getSecurityProfileFactory(bankRole);
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
