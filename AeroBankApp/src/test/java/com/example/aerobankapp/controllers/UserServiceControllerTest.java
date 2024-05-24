package com.example.aerobankapp.controllers;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.workbench.utilities.LoginRequest;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.example.aerobankapp.controllers.AuthControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = UserServiceController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
class UserServiceControllerTest
{
    @MockBean
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    @WithMockUser
    public void getListOfUsers() throws Exception
    {
        UserEntity user1 = UserEntity.builder()
                .userID(1)
                .userSecurity(UserSecurity.builder().role(Role.ADMIN).isAdmin(true).pinNumber("5988").isEnabled(true).build())
                .userCredentials(UserCredentials.builder().password("Halflifer45!").username("AKing94").build())
                .userDetails(UserDetails.builder().email("alex@utahkings.com").accountNumber("87-34-21").build())
                .build();

        UserEntity user2 = UserEntity.builder()
                .userID(2)
                .userDetails(UserDetails.builder().accountNumber("34-22-56").email("BSmith@outlook.com").build())
                .userCredentials(UserCredentials.builder().password("pass").username("BSmith23").build())
                .userSecurity(UserSecurity.builder().isEnabled(true).isAdmin(false).pinNumber("2233").role(Role.USER).build())
                .build();

        List<UserEntity> userList = Arrays.asList(user1, user2);

        given(userService.findAll()).willReturn(userList);

        mockMvc.perform(get("/api/users/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getListOfUserNames() throws Exception
    {
        String username1 = "AKing94";
        String username2 = "BSmith23";

        List<String> usernamesList = Arrays.asList(username1, username2);

        given(userService.getListOfUserNames()).willReturn(usernamesList);

        mockMvc.perform(get("/api/users/user-names-list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void saveUserToDatabase() throws Exception
    {
        // Prepare a UserRequest object (assuming it's a simple POJO)
        UserRequest request = new UserRequest();
        request.setUser("Adam553");
        request.setFirstName("Adam");
        request.setLastName("West");
        request.setRole("USER");
        request.setPin("2233");
        request.setEmail("adam@outlook.com");
        request.setPass("password");

        // Convert the object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // Perform the POST request and expect a 200 OK response
        mockMvc.perform(post("/api/users/save")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been successfully saved"))
                .andDo(print());

        // Verify that userService.save was called with the right UserEntity
        UserEntity expectedUserEntity = UserEntity.builder()
                .userSecurity(UserSecurity.builder().pinNumber(request.getPin()).isEnabled(true).role(Role.valueOf(request.getRole())).build())
                .userCredentials(UserCredentials.builder().username(request.getUser()).password(request.getPass()).build())
                .userDetails(UserDetails.builder().email(request.getEmail()).build())
                .build();

        verify(userService).save(refEq(expectedUserEntity));
    }

    @AfterEach
    void tearDown() {
    }
}