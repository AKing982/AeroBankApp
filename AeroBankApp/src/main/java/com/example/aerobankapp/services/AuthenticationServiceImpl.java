package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.nimbusds.jwt.JWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthenticationServiceImpl implements AuthenticationProvider
{
    private JdbcTemplate jdbcTemplate;
    private JWTUtil jwtUtil = new JWTUtil();
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
    {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public AuthenticationServiceImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userInDatabase(String user)
    {
        String findUser = "SELECT username FROM Users WHERE username=?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(findUser, Boolean.class, user));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = user
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
