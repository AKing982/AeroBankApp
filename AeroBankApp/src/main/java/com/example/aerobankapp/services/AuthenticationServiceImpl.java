package com.example.aerobankapp.services;

import com.example.aerobankapp.credentials.Credentials;
import com.example.aerobankapp.workbench.utilities.UserType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class AuthenticationServiceImpl implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private AeroLogger aeroLogger;

    @Autowired
    public AuthenticationServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userService)
    {
        this.userDetailsService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.aeroLogger = new AeroLogger(AuthenticationServiceImpl.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
       UserDetails userDetails = getUserDetailsService().loadUserByUsername(authentication.getName());
       final String foundUserName = userDetails.getUsername().trim();
       final String foundPassword = userDetails.getPassword().trim();
       final String authName = authentication.getName().trim();
       final String authCredentials = authentication.getCredentials().toString().trim();
       final Collection<? extends GrantedAuthority> grantedRoles = userDetails.getAuthorities();
       aeroLogger.info("Credentials: " + authCredentials);
       aeroLogger.info("UserName: " + authName);
       aeroLogger.info("Found User: " + foundUserName);
       aeroLogger.info("Found Password: " + foundPassword);

       if(authCredentials.equals(foundPassword) && authName.equals(foundUserName))
       {
           return new UsernamePasswordAuthenticationToken(authName, authCredentials);
       }
       else
       {
           throw new BadCredentialsException("Invalid Username or Password");
       }

    }

    private Credentials<String> getUserCredentials()
    {
        return null;
    }

    private Credentials<String> getAuthenticationCredentials(Authentication authentication)
    {
        return new Credentials<>(authentication.getName(), authentication.getCredentials().toString());
    }

    private Map<String, String> getAuthCredentials()
    {
        return null;
    }


    public boolean isAuthenticated(Authentication authentication)
    {
        return authentication.isAuthenticated();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
