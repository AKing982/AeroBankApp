package com.example.aerobankapp.workbench.threadServices;

import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.services.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testExistingUserAuthenticate()
    {

        String user = "AKing94";
        String pass = "Halflifer45!";

        boolean isAuthenticated = loginService.authenticateUser(user, pass);

        assertTrue(isAuthenticated);
    }

    @AfterEach
    void tearDown() {
    }
}