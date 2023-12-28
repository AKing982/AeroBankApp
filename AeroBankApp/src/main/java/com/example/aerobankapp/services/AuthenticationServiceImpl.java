package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthenticationServiceImpl implements AuthenticationProvider
{
    private UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil = new JWTUtil();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
       UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

       // Generate a Token
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return authentication;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
