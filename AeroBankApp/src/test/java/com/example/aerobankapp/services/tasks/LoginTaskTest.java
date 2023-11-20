package com.example.aerobankapp.services.tasks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

@RunWith(SpringRunner.class)
@SpringBootTest
class LoginTaskTest
{
    @MockBean
    private LoginTask loginTask;

    @BeforeEach
    void setUp()
    {
        loginTask = new LoginTask();
    }

    @Test
    public void executeTask()
    {
        loginTask.execute();
    }


    @AfterEach
    void tearDown() {
    }
}