package com.example.aerobankapp.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(value=UserProfileController.class,
excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserProfileController userProfileController;

    @BeforeEach
    void setUp() {
    }


    @AfterEach
    void tearDown() {
    }
}