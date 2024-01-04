package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class AuthenticationServiceImpl implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userService)
    {
        this.userDetailsService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
       UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
       final String foundUserName = userDetails.getUsername();
       final String foundPassword = userDetails.getPassword();
       final String authName = authentication.getName();
       final String authCredentials = authentication.getCredentials().toString();
       if(passwordEncoder.matches(authCredentials, foundPassword) && authName.matches(foundUserName))
       {
           return new UsernamePasswordAuthenticationToken(authName, authCredentials);
       }
       else
       {
           throw new BadCredentialsException("Invalid Username or Password");
       }

    }

    public boolean isAuthenticated(Authentication authentication)
    {
        if(authentication.isAuthenticated())
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
