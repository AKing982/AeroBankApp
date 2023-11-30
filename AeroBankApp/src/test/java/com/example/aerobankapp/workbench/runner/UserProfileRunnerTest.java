package com.example.aerobankapp.workbench.runner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserProfileRunnerTest
{
    @MockBean
    private UserProfileRunner userProfileRunner;

    @BeforeEach
    void setUp()
    {
        userProfileRunner = new UserProfileRunner();
    }

    @AfterEach
    void tearDown() {
    }
}