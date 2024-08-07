package com.example.aerobankapp.controllers;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.services.WithdrawService;
import com.example.aerobankapp.workbench.utilities.Status;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(value= WithdrawController.class)
@RunWith(SpringRunner.class)
@Import({JpaConfig.class, AppConfig.class})
class WithdrawControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WithdrawService withdrawService;

    private WithdrawEntity mockWithdraw;
    private int mockValidUserID = 1;

    @BeforeEach
    void setUp() {
        mockWithdraw = createMockWithdraw(1L, 1, 1, "Test Withdrawal", new BigDecimal("45.00"));
    }

    @Test
    @WithMockUser
    public void test_GetWithdrawalByUserID_InvalidUserID() throws Exception {
        mockMvc.perform(get("/api/withdraw/{userID}", -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void test_GetWithdrawalByUserID_ValidUserID() throws Exception {
        int userID = 1;
        when(withdrawService.findByUserID(userID)).thenReturn(List.of(mockWithdraw));

        mockMvc.perform(get("/api/withdraw/user/{userID}", userID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void test_GetWithdrawalByAcctID_InvalidAccountID_ReturnBadRequest() throws Exception {
        final int acctID = -1;
        mockMvc.perform(get("/api/withdraw/account/{acctID}", acctID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void test_GetWithdrawalsByAcctID_ValidAccountID_ReturnOk() throws Exception {
        final int acctID = 1;

        when(withdrawService.findByAccountID(acctID)).thenReturn(List.of(mockWithdraw));

        mockMvc.perform(get("/api/withdraw/account/{acctID}", acctID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }



    @Test
    @WithMockUser
    public void testFindWithdrawalsBetweenDates_WithResults() throws Exception{
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 1);
        List<WithdrawEntity> mockResults = List.of(mockWithdraw);

        given(withdrawService.findWithdrawBetweenDates(startDate, endDate)).willReturn(mockResults);

        mockMvc.perform(get("/api/withdraw/between")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(mockResults.size()));
    }

    @Test
    @WithMockUser
    public void testFindWithdrawalsBetweenDates_NoResults() throws Exception {
        LocalDate startDate = LocalDate.of(2021, 2, 1);
        LocalDate endDate = LocalDate.of(2021, 2, 28);

        given(withdrawService.findWithdrawBetweenDates(startDate, endDate)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/withdraw/between")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]")); // Expecting an empty array
    }

    @Test
    @WithMockUser
    public void testFindWithdrawalsByUserIDAscending_InvalidUserID_Return_Bad_Request() throws Exception {
        final int userID = -1;

        mockMvc.perform(get("/api/withdraw/{userID}/ascending", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFindWithdrawalsByUserIDAscending_ValidUserID_ReturnOk() throws Exception{
        final int userID = 1;

        when(withdrawService.getListOfWithdrawalsByUserIDAsc(userID)).thenReturn(List.of(mockWithdraw));

        mockMvc.perform(get("/api/withdraw/{userID}/ascending", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void testFindWithdrawalByUserIDDescending_InvalidUserID_Return_Bad_Request() throws Exception{
        final int userID = -1;

        mockMvc.perform(get("/api/withdraw/{userID}/descending", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFindWithdrawalByUserIDDescending_ValidUserID_Return_StatusOk() throws Exception{
        final int userID = 1;
        when(withdrawService.getListOfWithdrawalsByUserIDDesc(userID)).thenReturn(List.of(mockWithdraw));

        mockMvc.perform(get("/api/withdraw/{userID}/descending", userID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalByUserName_EmptyStringUser_ReturnNotFound() throws Exception {
        final String user = "";

        mockMvc.perform(get("/api/withdraw/name/{user}", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Disabled
    public void testGetWithdrawalByUserName_NonExistingUser_Return_BadRequest() throws Exception {
        final String user = "Mike23";

        when(withdrawService.findByUserName(user)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/withdraw/name/{user}", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalByStatus_NullStatus_Return_NotFound() throws Exception {

        mockMvc.perform(get("/api/withdraw/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsByStatus_ActiveStatus_Return_StatusOk() throws Exception {
        Status status = Status.ACTIVE;
        when(withdrawService.findByStatus(status)).thenReturn(List.of(mockWithdraw));

        mockMvc.perform(get("/api/withdraw/status")
                .param("status", status.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testAddWithdrawal_NullWithdrawDTO_Return_Forbidden() throws Exception {

        mockMvc.perform(post("/api/withdraw/submit")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }




    private WithdrawEntity createMockWithdraw(Long id, int userID, int acctID, String description, BigDecimal amount)
    {
        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setWithdrawID(id);
        withdrawEntity.setAccount(AccountEntity.builder().acctID(acctID).build());
        withdrawEntity.setUser(UserEntity.builder().userID(userID).build());
       // withdrawEntity.setAmount(amount);
     //   withdrawEntity.setDescription(description);
      //  withdrawEntity.setPosted(LocalDate.now());
      //  withdrawEntity.setStatus(TransactionStatus.PENDING);
        return withdrawEntity;
    }

    @AfterEach
    void tearDown() {
    }
}