package com.example.aerobankapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean authenticate(String user, String pass)
    {
        String query = "SELECT COUNT(*) FROM users WHERE username=? AND password=?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, user, pass);
        return count == 1;
    }
}
