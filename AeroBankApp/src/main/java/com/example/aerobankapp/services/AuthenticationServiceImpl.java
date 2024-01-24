package com.example.aerobankapp.services;

import com.example.aerobankapp.credentials.Credentials;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.utilities.UserType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
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
    private JWTUtil jwtUtil = new JWTUtil();
    private AeroLogger aeroLogger;

    @Autowired
    public AuthenticationServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userService)
    {
        this.userDetailsService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.aeroLogger = new AeroLogger(AuthenticationServiceImpl.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        aeroLogger.info("Authentication Name: " + authentication.getName());
       UserDetails userDetails = loadUserDetails(authentication.getName());
       final String foundUserName = userDetails.getUsername().trim();
       final String foundPassword = userDetails.getPassword().trim();
       final String authName = authentication.getName().trim();
       final String authCredentials = authentication.getCredentials().toString().trim();
       final Collection<? extends GrantedAuthority> grantedRoles = userDetails.getAuthorities();
       aeroLogger.info("UserDetails: " + userDetails.toString());
       aeroLogger.info("Credentials: " + authCredentials);
       aeroLogger.info("UserName: " + authName);
       aeroLogger.info("Found User: " + foundUserName);
       aeroLogger.info("Found Password: " + foundPassword);
       aeroLogger.info("User Roles: " + Arrays.toString(grantedRoles.toArray()));

       if(getPasswordEncoder().matches(authCredentials, foundPassword) && authName.equals(foundUserName))
       {
           aeroLogger.warn("Password Matches: " + getPasswordEncoder().matches(authCredentials, foundPassword));
           return new UsernamePasswordAuthenticationToken(authName, null, grantedRoles);
       }
       else
       {
           throw new BadCredentialsException("Invalid Username or Password");
       }

    }

    public UserDetails loadUserDetails(String user)
    {
        return getUserDetailsService().loadUserByUsername(user);
    }

    public String login(String user, String password)
    {
        Authentication authentication = authenticate(new UsernamePasswordAuthenticationToken(user, password));
        if(authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(authentication);
            if(token == null)
            {
                aeroLogger.error("Failed to generated token for user: {}", user);
                throw new AuthenticationServiceException("Token Generation failed");
            }
            aeroLogger.warn("Token: " + token);
            return token;
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
