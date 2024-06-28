package com.example.aerobankapp.services;

import com.example.aerobankapp.credentials.Credentials;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.utilities.UserType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import com.example.aerobankapp.workbench.utilities.response.AuthDataResponse;
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
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@RequestScope
public class AuthenticationServiceImpl implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private UserService userDAO;
    private PlaidAccountsServiceImpl plaidAccountsService;
    private JWTUtil jwtUtil = new JWTUtil();
    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    public AuthenticationServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userService,
                                     UserService userDAO,
                                     PlaidAccountsServiceImpl plaidAccountsService)
    {
        this.userDetailsService = userService;
        this.userDAO = userDAO;
        this.plaidAccountsService = plaidAccountsService;
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

    public AuthDataResponse login(String user, String password)
    {
        Authentication authentication = authenticate(new UsernamePasswordAuthenticationToken(user, password));
        if(authentication.isAuthenticated()) {

            String uuidToken = UUID.randomUUID().toString();
            int userID = userDAO.getUserIDByUserName(user);
            Boolean hasPlaidAccount = plaidAccountsService.hasPlaidAccount(userID);
            return new AuthDataResponse(uuidToken, "Bearer", hasPlaidAccount, user, authentication.getAuthorities());
        }
        else
        {
            throw new BadCredentialsException("Invalid Username or Password");
        }
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
