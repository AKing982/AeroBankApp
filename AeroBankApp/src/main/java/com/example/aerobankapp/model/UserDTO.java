package com.example.aerobankapp.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class UserDTO implements Serializable, UserDetails
{
    private static long serialVersionUID = 1L;

    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private char[] password;
    private int pinNumber;
    private boolean isAdmin;
    private boolean isEnabled;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonLocked;
    private boolean isAccountNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername()
    {
        return user;
    }

    @Override
    public String getPassword()
    {
        return password.toString();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }
}
