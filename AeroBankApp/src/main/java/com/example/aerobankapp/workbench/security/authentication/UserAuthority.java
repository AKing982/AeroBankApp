package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@AllArgsConstructor
public class UserAuthority implements UserDetails
{
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private boolean isAdmin;
    private boolean isDepositEnabled;
    private boolean isWithdrawEnabled;
    private boolean isLoanApproved;
    private boolean isPurchaseEnabled;
    private boolean isSchedulingAllowed;
    private boolean isInactive;
    private final User secureUser;
    private Collection<? extends GrantedAuthority> authorities;

    private UserAuthority(User user)
    {
        this.secureUser = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return secureUser.getPassword().toString();
    }

    @Override
    public String getUsername() {
        return secureUser.getUser();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public static UserAuthority createUserAuthority()
    {
        return new UserAuthorityBuilder()
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAdmin(false)
                .isDepositEnabled(true)
                .isAccountNonLocked(true)
                .isWithdrawEnabled(true)
                .isLoanApproved(false)
                .build();
    }

    public static UserAuthority createAdminAuthority()
    {
        return new UserAuthorityBuilder()
                .isEnabled(true)
                .isLoanApproved(false)
                .isDepositEnabled(true)
                .isWithdrawEnabled(true)
                .isAdmin(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isAccountNonExpired(true)
                .build();
    }

}
