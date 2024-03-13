package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dao.UserLogService;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value= UserLogControllerTest.class)
class UserLogControllerTest
{
    @MockBean
    private UserLogController userLogController;

    @MockBean
    private UserLogService userLogService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void getUserLogsById_ThenReturnUserLog() throws Exception {
        int id = 1;
        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setUserEntity(UserEntity.builder().userID(1).username("AKing94").build());
        userLogEntity.setId(1);
        userLogEntity.setLoginSuccess(true);
        userLogEntity.setLoginAttempts(1);
        userLogEntity.setLastLogout(LocalDateTime.of(2024, 3, 4, 5, 2));
        userLogEntity.setLastLogin(LocalDateTime.of(2024, 3, 4, 5, 2));
        userLogEntity.setSessionDuration(6049);

        when(userLogService.findAllById(1L)).thenReturn(Optional.of(userLogEntity));

        mockMvc.perform(get("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.sessionID").value(1))
                .andExpect(jsonPath("$.userID").value(userLogEntity.getUserEntity().getUserID()))
                .andExpect(jsonPath("$.username").value("AKing94"))
                .andExpect(jsonPath("$.loginSuccess").value(true))
                .andExpect(jsonPath("$.loginAttempts").value(1))
                .andExpect(jsonPath("$.lastLogout").value("2024-03-04T05:02:00"))
                .andExpect(jsonPath("$.lastLogin").value("2024-03-04T05:02:00"))
                .andExpect(jsonPath("$.sessionToken").value("ajajajsdfasdf"))
                .andExpect(jsonPath("$.sessionDuration").value(6049));


    }

    @AfterEach
    void tearDown() {
    }
}