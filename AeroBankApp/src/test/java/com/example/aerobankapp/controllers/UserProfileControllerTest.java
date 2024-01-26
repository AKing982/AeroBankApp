package com.example.aerobankapp.controllers;

import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.User;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.aerobankapp.controllers.AuthControllerTest.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value=UserProfileController.class,
excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserProfileController userProfileController;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getUserProfileData_ValidUserName() throws Exception
    {
        String username = "AKing94";
        String email = "alex@utahkings.com";
        String pin = "5988";
        String password = "Halflifer45!";
        Role role = Role.ADMIN;

        User user = User.builder()
                .userName(username)
                .email(email)
                .password(password)
                .pinNumber(pin)
                .role(role)
                .build();

        UserProfile userProfile = new UserProfile(user);

        mockMvc.perform(get("http://localhost:8080/api/profile/data/" + username)
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }


    @AfterEach
    void tearDown() {
    }
}