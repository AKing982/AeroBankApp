package com.example.aerobankapp.workbench.model;

import com.example.aerobankapp.workbench.security.authentication.SecurityConfig;
import org.checkerframework.common.util.report.qual.ReportUnqualified;
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
class LoginModelTest
{
    @MockBean
    private LoginModel loginModel;

    @Autowired
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp()
    {

    }

    @AfterEach
    void tearDown() {
    }
}