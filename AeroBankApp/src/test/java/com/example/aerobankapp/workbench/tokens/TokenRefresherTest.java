package com.example.aerobankapp.workbench.tokens;

import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class TokenRefresherTest
{
    private TokenRefresher tokenRefresher;
    private JWTUtil jwtUtil = new JWTUtil();
    private String testToken = jwtUtil.generateToken("AKing94");

    @BeforeEach
    void setUp()
    {
        tokenRefresher = new TokenRefresher();
    }

    @Test
    public void testToken()
    {

    }

    @AfterEach
    void tearDown() {
    }
}