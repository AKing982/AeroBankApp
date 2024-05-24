package com.example.aerobankapp.controllers;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.workbench.utilities.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value= UserLogControllerTest.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
class UserLogControllerTest
{
    @MockBean
    private UserLogController userLogController;

    @MockBean
    private UserLogServiceImpl userLogService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void getUserLogsById_ThenReturnUserLog() throws Exception {
        int id = 1;
        LocalDateTime lastLogin = LocalDateTime.of(2024, 3, 4, 5, 2);
        LocalDateTime lastLogout = LocalDateTime.of(2024, 3, 4, 5, 2);

        UserDetails userDetails = UserDetails.builder()
                .firstName("Alex")
                .lastName("King")
                .email("alex@utahkings.com")
                .build();

        UserCredentials userCredentials = UserCredentials.builder()
                .username("AKing94")
                .password("Halflifer45!")
                .build();

        UserSecurity userSecurity = UserSecurity.builder()
                .isAdmin(true)
                .isEnabled(true)
                .role(Role.ADMIN)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .userID(1)
                .userDetails(userDetails)
                .userCredentials(userCredentials)
                .userSecurity(userSecurity)
                .build();

        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setUserEntity(userEntity);
        userLogEntity.setId(1);
        userLogEntity.setLoginSuccess(true);
        userLogEntity.setLoginAttempts(1);
        userLogEntity.setLastLogout(String.valueOf(lastLogout));
        userLogEntity.setLastLogin(String.valueOf(lastLogin));
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
                .andExpect(jsonPath("$.lastLogout").value(lastLogout.toString()))
                .andExpect(jsonPath("$.lastLogin").value(lastLogin.toString()))
                .andExpect(jsonPath("$.sessionDuration").value(6049));
    }

    @Test
    public void getUserLogByNumberOfLoginAttempts_InitialTest_Failure() throws Exception {
        int attempts = -2; // Invalid number of attempts
        int userID = -2; // Invalid userID

        given(userLogService.getUserLogByNumberOfLoginAttempts(attempts, userID)).willReturn(null);

        String attemptsStr = String.valueOf(attempts);
        String userIDStr = String.valueOf(userID);
        System.out.println("Attempts: " + attemptsStr);
        System.out.println("UserID:  " + userIDStr);
        mockMvc.perform(get("/api/session/byAttempts")
                .param("attempts", attemptsStr)
                        .param("userID", userIDStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request due to invalid parameters
    }


    @Test
    public void findUserLogEntriesByActiveStateAndUserID_InvalidUserID() throws Exception {
        final int userID = -1;
        final boolean isActive = true;

        given(userLogService.findUserLogEntriesByActiveStateAndUserID(isActive, userID)).willReturn(null);

        mockMvc.perform(get("/api/session/byActiveState")
                .param("IsActive", String.valueOf(isActive))
                .param("UserID", String.valueOf(userID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //verify(userLogService).findUserLogEntriesByActiveStateAndUserID(isActive, userID);
    }

    @Test
    public void findUserLogEntriesByActiveStateAndUserID_ValidUserID_returnNotFound() throws Exception {
        final int userID = 2;
        final boolean isActive = true;

        given(userLogService.findUserLogEntriesByActiveStateAndUserID(isActive, userID)).willReturn(null);

        mockMvc.perform(get("/api/session/byActiveState")
                .param("IsActive", String.valueOf(isActive))
                .param("UserID", String.valueOf(userID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getUserLogsByLastLogin_InvalidID_ValidLastLogin_return_NotFound() throws Exception {
        final Long id = -1L;
        final String date = "2024-03-15 12:56:21";
        final LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 12, 56, 21);

        given(userLogService.getUserLogsByLastLogin(id, dateTime)).willReturn(null);

        mockMvc.perform(get("/api/session/byLastLogin")
                .param("Id", String.valueOf(id))
                .param("LastLogin", String.valueOf(dateTime))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getUserLogsByLastLogin_ValidUserID_ValidDate_return_UserLog() throws Exception {
        final Long id = 2L;
        final LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 12, 56, 21);

        UserDetails userDetails = UserDetails.builder()
                .firstName("Alex")
                .lastName("King")
                .email("alex@utahkings.com")
                .build();

        UserCredentials loginCredentials = UserCredentials.builder()
                .username("AKing94")
                .password("Halflifer45!")
                .build();

        UserSecurity userSecurity = UserSecurity.builder()
                .isAdmin(true)
                .isEnabled(true)
                .role(Role.ADMIN)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .userID(1)
                .userDetails(userDetails)
                .userCredentials(loginCredentials)
                .userSecurity(userSecurity)
                .build();

        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setUserEntity(userEntity);
        userLogEntity.setId(1);
        userLogEntity.setLoginSuccess(true);
        userLogEntity.setLoginAttempts(1);
        userLogEntity.setLastLogout(String.valueOf(dateTime));
        userLogEntity.setLastLogin(String.valueOf(dateTime));
        userLogEntity.setSessionDuration(5000);

        given(userLogService.getUserLogsByLastLogin(id, dateTime)).willReturn(List.of(userLogEntity));

        mockMvc.perform(get("/api/session/byLastLogin")
                        .param("id", String.valueOf(id))
                        .param("LastLogin", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void testUpdateUserLog() throws Exception{
        Long id = 1L;
        LocalDateTime lastLogin = LocalDateTime.of(2024, 3, 15, 12, 56, 21);
        LocalDateTime lastLogout = LocalDateTime.of(2024, 3, 15, 16, 30, 12);

    //    UserLogDTO userLogDTO = new UserLogDTO(1L,1, false, lastLogin, lastLogout, 1, false, 5);

     //   doNothing().when(userLogService).updateUserLog(id, false, lastLogin, lastLogout, 5, false, 1);

        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());
     //   String userLogDTOJson = objectMapper1.writeValueAsString(userLogDTO);

    //    mockMvc.perform(put("/api/session/updateUserLog/{id}", id)
    //            .contentType(MediaType.APPLICATION_JSON)
     //           .content(userLogDTOJson))
     //           .andExpect(status().isOk());

        //verify(userLogService).updateUserLog(id, true, lastLogin, lastLogout, 1, true, 5000);

    }




    @Test
    public void findUserLogEntriesByActiveStateAndUserID_ValidUserID() throws Exception {
        final int userID = 1;
        final boolean isActive = true;

        UserDetails userDetails = UserDetails.builder()
                .firstName("Alex")
                .lastName("King")
                .email("alex@utahkings.com")
                .build();

        UserCredentials loginCredentials = UserCredentials.builder()
                .username("AKing94")
                .password("Halflifer45!")
                .build();

        UserSecurity userSecurity = UserSecurity.builder()
                .isAdmin(true)
                .isEnabled(true)
                .role(Role.ADMIN)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .userID(userID)
                .userDetails(userDetails)
                .userCredentials(loginCredentials)
                .userSecurity(userSecurity)
                .build();

        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setUserEntity(userEntity);
        userLogEntity.setId(1);
        userLogEntity.setLoginSuccess(true);
        userLogEntity.setLoginAttempts(1);
        userLogEntity.setSessionDuration(6049);
        userLogEntity.setLastLogout(String.valueOf(LocalDateTime.of(2024, 3, 4, 5, 2)));
        userLogEntity.setLastLogin(String.valueOf(LocalDateTime.of(2024, 3, 15, 12, 56, 21)));

        given(userLogService.findUserLogEntriesByActiveStateAndUserID(isActive, userID)).willReturn(Optional.of(userLogEntity));

        mockMvc.perform(get("/api/session/byActiveState")
                        .param("isActive", String.valueOf(isActive))
                        .param("userID", String.valueOf(userID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testFindCurrentUserLogSessionByUserID_InvalidUserID() throws Exception {
        final int userID = 1;
        final boolean isActive = true;

        UserDetails userDetails = UserDetails.builder()
                .firstName("Alex")
                .lastName("King")
                .email("alex@utahkings.com")
                .build();

        UserCredentials loginCredentials = UserCredentials.builder()
                .username("AKing94")
                .password("Halflifer45!")
                .build();

        UserSecurity userSecurity = UserSecurity.builder()
                .isAdmin(true)
                .isEnabled(true)
                .role(Role.ADMIN)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .userDetails(userDetails)
                .userCredentials(loginCredentials)
                .userSecurity(userSecurity)
                .userID(userID)
                .build();

        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setUserEntity(userEntity);
        userLogEntity.setId(1);
        userLogEntity.setLoginSuccess(true);
        userLogEntity.setLoginAttempts(1);
        userLogEntity.setLastLogout(String.valueOf(LocalDateTime.of(2024, 3, 4, 5, 2)));
        userLogEntity.setLastLogin(String.valueOf(LocalDateTime.of(2024, 3, 15, 12, 56, 21)));
        userLogEntity.setSessionDuration(6049);

        given(userLogService.findUserLogEntriesByActiveStateAndUserID(isActive, userID)).willReturn(Optional.of(userLogEntity));

        mockMvc.perform(get("/api/session/byActiveState")
                        .param("isActive", String.valueOf(isActive))
                        .param("userID", String.valueOf(userID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }




    @AfterEach
    void tearDown() {
    }
}