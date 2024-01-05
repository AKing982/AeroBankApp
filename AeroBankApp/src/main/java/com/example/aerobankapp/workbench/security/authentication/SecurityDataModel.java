package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.BankAuthorization;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Component
@Getter
@Setter
public class SecurityDataModel
{
    private Collection<GrantedAuthority> grantedAuthorities;
    private UserSecurityProfile userSecurityProfile;
    private Set<BankAuthorization> bankRolesPermissions;
    private LocalDateTime lastProfileUpdate;
    private LocalDateTime lastLoginTime;
    private SecurityType securityType;

    private long sessionID;
    private boolean isAuthorized;

    public SecurityDataModel(Collection<GrantedAuthority> authorities, UserSecurityProfile userSecurityProfile)
    {
        this.grantedAuthorities = authorities;
        this.userSecurityProfile = userSecurityProfile;
    }

    public SecurityDataModel()
    {

    }

    public LocalDateTime recordLastProfileUpdate()
    {
        return null;
    }

    public LocalDateTime lastLoginTime()
    {
        return null;
    }

    public void setUserSecurityProfile(UserSecurityProfile securityProfile)
    {
        this.userSecurityProfile = securityProfile;
    }

    public void setBankRolePermissions(BankAuthorization roles)
    {
        bankRolesPermissions.add(roles);
    }

    public void setGrantedAuthorities(GrantedAuthority grantedAuthority)
    {
        grantedAuthorities.add(grantedAuthority);
    }

    public boolean isAuthorized(GrantedAuthority authority, boolean isAuthorized)
    {
        return false;
    }

}
