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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
class LoginServiceImplTest
{
    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private LoginServiceImpl loginService;


    @BeforeEach
    void setUp()
    {
        loginService = new LoginServiceImpl(userService);
    }

    @Test
    public void testAuthenticatingUser()
    {
        String testUser = "AKing94";
        String password = "Halflifer94!";

        boolean isAuthenticated = loginService.authenticateUser(testUser, password);

        assertTrue(isAuthenticated);
    }



    @AfterEach
    void tearDown() {
    }
}