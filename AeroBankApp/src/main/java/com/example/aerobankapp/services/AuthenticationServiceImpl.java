package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.nimbusds.jwt.JWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthenticationServiceImpl implements AuthenticationService
{

    private JdbcTemplate jdbcTemplate;
    private JWTUtil jwtUtil = new JWTUtil();

    @Autowired
    public AuthenticationServiceImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean authenticateByUserCount(String user, String pass)
    {

       String query = "SELECT COUNT(*) FROM users WHERE username=? AND password=?";
       int count = jdbcTemplate.queryForObject(query, Integer.class, user, pass);
        return count == 1;
    }

    @Override
    public boolean authenticate(final String user, final String pass)
    {
        String token = jwtUtil.generateToken(user);

        boolean isValidToken = jwtUtil.validateToken(token);
        boolean isCountValid = authenticateByUserCount(user, pass);

        if(isValidToken && isCountValid)
        {
            // Valid user is in database
            boolean userInDB = userInDatabase(user);
            if(userInDB)
            {
                return true;
            }
        }
        return false;
    }

    public boolean userInDatabase(String user)
    {
        String findUser = "SELECT username FROM Users WHERE username=?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(findUser, Boolean.class, user));
    }

}
