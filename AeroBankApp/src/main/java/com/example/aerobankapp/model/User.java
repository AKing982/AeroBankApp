package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.CardDesignator;
import javafx.scene.image.ImageView;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class User implements Serializable, UserDetails
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
    private List<CardDesignator> cards = new ArrayList<>();
    private List<ImageView> cardImages = new ArrayList<>();

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
