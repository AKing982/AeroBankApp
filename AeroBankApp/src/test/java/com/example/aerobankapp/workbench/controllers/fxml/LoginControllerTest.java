package com.example.aerobankapp.workbench.controllers.fxml;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LoginControllerTest
{
    @MockBean
    private LoginController loginController;

    @Autowired
    private LoginModel loginModel;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}