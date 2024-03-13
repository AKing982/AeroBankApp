package com.example.aerobankapp.services;

import com.example.aerobankapp.repositories.UserLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserLogServiceImplTest {

    @InjectMocks
    private UserLogServiceImpl userLogService;

    @Autowired
    private UserLogRepository userLogRepository;

    @BeforeEach
    void setUp() {
        userLogService = new UserLogServiceImpl(userLogRepository);
    }


    @AfterEach
    void tearDown() {
    }
}