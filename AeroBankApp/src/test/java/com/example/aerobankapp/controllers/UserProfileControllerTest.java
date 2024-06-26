package com.example.aerobankapp.controllers;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.UserProfileDataService;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.User;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.checkerframework.common.util.report.qual.ReportUnqualified;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.aerobankapp.controllers.AuthControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

@WebMvcTest(value=UserProfileController.class)
@RunWith(SpringRunner.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserProfileDataService userProfileDataService;

    private UserProfileController userProfileController;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        userProfileController = new UserProfileController(userService, accountService, userProfileDataService);
    }

    @Test
    void getUserProfileData_ValidUserName() throws Exception
    {
        String username = "AKing94";
        String email = "alex@utahkings.com";
        String pin = "5988";
        String password = "Halflifer45!";
        Role role = Role.ADMIN;

        UserEntity userEntity = UserEntity.builder()
                .userSecurity(UserSecurity.builder().pinNumber(pin).role(role).build())
                .userDetails(UserDetails.builder().email(email).build())
                .userCredentials(UserCredentials.builder().username(username).password(password).build())
                .userID(1)
                .build();


        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);

        when(userService.findByUserName(username)).thenReturn(userEntityList);

        mockMvc.perform(get("/api/profile/" + username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    @WithMockUser
    public void testWithAnotherValidUser() throws Exception {
        String username = "BSmith23";
        String email = "bsmith@outlook.com";
        String pin = "2233";
        String password = "pass";
        Role role = Role.USER;

        UserEntity userEntity = UserEntity.builder()
                .userID(2)
                .userCredentials(UserCredentials.builder().password("pass").username("BSmith23").build())
                .userSecurity(UserSecurity.builder().isEnabled(true).isAdmin(false).pinNumber("2233").role(Role.USER).build())
                .userDetails(UserDetails.builder().accountNumber("37-22-21").build())
                .build();

        List<UserEntity> userEntityList = Collections.singletonList(userEntity);

        when(userService.findByUserName(username)).thenReturn(userEntityList);

        mockMvc.perform(get("/api/profile/" + username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.username", is(username)));
    }


    @AfterEach
    void tearDown() {
    }
}