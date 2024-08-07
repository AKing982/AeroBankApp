package com.example.aerobankapp.workbench.security.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class will relate the GrantedAuthority to the UserSecurityProfile
 * which will determine what the current user is authorized to access within the application
 */

@Data
public class AuthorityProfileMapper
{
    private Map<GrantedAuthority, UserSecurityProfile> authorityProfileMap;

    public AuthorityProfileMapper()
    {
        this.authorityProfileMap = new HashMap<>();
    }

    public void addSecurityProfile(GrantedAuthority authority, UserSecurityProfile securityProfile)
    {
        Objects.requireNonNull(authority, "Authority cannot be null");
        Objects.requireNonNull(securityProfile, "SecurityProfile can't be null");

        authorityProfileMap.put(authority, securityProfile);
    }

    public String getRoleFromAuthority(GrantedAuthority grantedAuthority)
    {
        return grantedAuthority.getAuthority();
    }

    public UserSecurityProfile getUserSecurityProfile(GrantedAuthority authority)
    {
        return authorityProfileMap.get(authority);
    }

}
