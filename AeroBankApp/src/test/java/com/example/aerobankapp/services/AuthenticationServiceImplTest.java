package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceImplTest {

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp()
    {
        authenticationService = new AuthenticationServiceImpl(userDetailsService);
    }

    @Test
    public void testConstructor()
    {
        UserDetailsService userService = authenticationService.getUserDetailsService();

        assertNotNull(userService);
        assertNotNull(authenticationService);
    }

    @Test
    public void authenticateUserSuccess()
    {
        final String user = "AKing94";
        final String pass = "Halflifer94!";
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, pass);
        Authentication authenticatedToken = authenticationService.authenticate(authentication);
        boolean isAuthenticated = authenticatedToken.isAuthenticated();

        assertTrue(authenticatedToken.isAuthenticated());
        assertTrue(isAuthenticated);
    }

    @AfterEach
    void tearDown() {
    }
}