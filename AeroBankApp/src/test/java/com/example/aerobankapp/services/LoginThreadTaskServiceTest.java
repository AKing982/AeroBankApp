package com.example.aerobankapp.services;

import com.example.aerobankapp.configuration.TaskExecutorConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;

class LoginThreadTaskServiceTest
{
    @MockBean
    private LoginThreadTaskService loginThreadTaskService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}