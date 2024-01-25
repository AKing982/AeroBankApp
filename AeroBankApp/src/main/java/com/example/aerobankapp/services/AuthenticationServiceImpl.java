package com.example.aerobankapp.services;

import com.example.aerobankapp.credentials.Credentials;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.utilities.UserType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    public AuthenticationServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userService)
    {
        this.userDetailsService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        logger.info("Authentication Name: " + authentication.getName());
       UserDetails userDetails = loadUserDetails(authentication.getName());
       final String foundUserName = userDetails.getUsername().trim();
       final String foundPassword = userDetails.getPassword().trim();
       final String authName = authentication.getName().trim();
       final String authCredentials = authentication.getCredentials().toString().trim();
       final Collection<? extends GrantedAuthority> grantedRoles = userDetails.getAuthorities();
       logger.info("UserDetails: " + userDetails.toString());
       logger.info("Credentials: " + authCredentials);
       logger.info("UserName: " + authName);
       logger.info("Found User: " + foundUserName);
       logger.info("Found Password: " + foundPassword);
       logger.info("User Roles: " + Arrays.toString(grantedRoles.toArray()));

       if(getPasswordEncoder().matches(authCredentials, foundPassword) && authName.equals(foundUserName))
       {
           logger.warn("Password Matches: " + getPasswordEncoder().matches(authCredentials, foundPassword));
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
            String jwtToken = jwtUtil.generateToken(authentication);
            if(jwtToken == null)
            {
                logger.error("Failed to generated token for user: {}", user);
                throw new AuthenticationServiceException("Token Generation failed");
            }
            logger.warn("Token: " + jwtToken);
            return jwtToken;
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
