package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.utilities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserDetailsServiceImplTest
{
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserServiceImpl userDAO;

    @BeforeEach
    void setUp()
    {
        userDetailsService = new UserDetailsServiceImpl(userDAO);
    }

    @Test
    public void testLoadByUserNameValidUser()
    {
        String testUser = "AKing94";
        UserDetails expected = User.builder()
                .username("AKing94")
                .password("Halflifer45!")
                .build();

        UserDetails actual = userDetailsService.loadUserByUsername(testUser);

        assertEquals(expected, actual);
    }

    @AfterEach
    void tearDown() {
    }
}