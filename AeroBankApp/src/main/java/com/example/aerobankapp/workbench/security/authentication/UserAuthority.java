package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.workbench.utilities.BankAuthorization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Getter
@AllArgsConstructor
public class UserAuthority implements UserDetails {
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
    private boolean isEmployeeManagementAllowed;
    private boolean isDepositApprovalRequired;
    private boolean isWithdrawApprovalRequired;
    private BankAuthorization bankAuthorization;
    private final UserDTO secureUser;
    private Collection<? extends GrantedAuthority> authorities;

    private UserAuthority(UserDTO user) {
        this.secureUser = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
      //  return secureUser.getPassword().toString();
        return "";
    }

    @Override
    public String getUsername() {
       // return secureUser.getUser();
        return "";
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

    public static UserAuthority createUserAuthority() {
        return new UserAuthorityBuilder()
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAdmin(false)
                .isDepositEnabled(true)
                .isAccountNonLocked(true)
                .isWithdrawEnabled(true)
                .isLoanApproved(false)
                .bankAuthorization(BankAuthorization.CUSTOMER)
                .build();
    }

    public static UserAuthority createAdminAuthority() {
        return new UserAuthorityBuilder()
                .isEnabled(true)
                .isLoanApproved(false)
                .isDepositEnabled(true)
                .isWithdrawEnabled(true)
                .isAdmin(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .bankAuthorization(BankAuthorization.ADMIN)
                .isAccountNonExpired(true)
                .build();
    }

    public static UserAuthority createTellerAuthority() {
        return new UserAuthorityBuilder()
                .bankAuthorization(BankAuthorization.TELLER)
                .isAdmin(false)
                .isEmployeeManagementAllowed(false)
                .isEnabled(true)
                .build();
    }

    public static UserAuthority createManagerAuthority()
    {
        return new UserAuthorityBuilder()
                .isAdmin(true)
                .bankAuthorization(BankAuthorization.MANAGER)
                .isEmployeeManagementAllowed(true)
                .isSchedulingAllowed(true)
                .isDepositApprovalRequired(true)
                .isWithdrawApprovalRequired(true)
                .build();

    }

    public static UserAuthority createCustomAuthority(boolean isEnabled, boolean isLoanApproved,
                                                      boolean isAdmin, boolean isAccountNonExpired,
                                                      boolean isAccountNonLocked, boolean isDepositEnabled,
                                                      boolean isPurchaseEnabled, boolean isSchedulingAllowed, boolean isWithdrawEnabled,
                                                      BankAuthorization authorizationType)
    {
        return new UserAuthorityBuilder()
                .isEnabled(isEnabled)
                .bankAuthorization(authorizationType)
                .isAdmin(isAdmin)
                .isDepositEnabled(isDepositEnabled)
                .isPurchaseEnabled(isPurchaseEnabled)
                .isWithdrawEnabled(isWithdrawEnabled)
                .isLoanApproved(isLoanApproved)
                .isAccountNonLocked(isAccountNonLocked)
                .isAccountNonExpired(isAccountNonExpired)
                .isSchedulingAllowed(isSchedulingAllowed)
                .build();

    }

}
