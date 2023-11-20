package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserLogRunnerTest
{
    @MockBean
    private UserLogRunner runner;

    @Autowired
    private UserLogServiceImpl userLogService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private LoginModel loginModel;

    @BeforeEach
    void setUp()
    {
        runner = new UserLogRunner(userLogService, userService, loginModel);
    }

    @Test
    public void testUserIDRetrieval()
    {
        int expectedID = 1;
        int actualID = runner.getCurrentUserID();

        assertNotNull(runner);
        assertEquals(expectedID, actualID);
    }

    @AfterEach
    void tearDown() {
    }
}