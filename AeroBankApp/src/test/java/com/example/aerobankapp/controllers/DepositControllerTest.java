package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.DepositServiceImpl;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.Role;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(value= DepositControllerTest.class)
@RunWith(SpringRunner.class)
class DepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositController depositController;

    @MockBean
    private DepositServiceImpl depositService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void whenDepositIsPosted_ThenReturnDepositResponse() throws Exception
    {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        DepositRequest depositDTO = new DepositRequest(1, "A1", "1414", today, now, "Once", "Transfer");


        mockMvc.perform(post("/api/deposits/create")
                .content(String.valueOf(depositDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountCode").value("A1"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getDeposits_ThenReturnDepositList() throws Exception
    {
        int acctID = 1;

        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        UserEntity mockUser = UserEntity.builder()
                .userID(1)
                .username("AKing94")
                .password("Halflifer45!")
                .email("alex@utahkings.com")
                .pinNumber("5988")
                .accountNumber("37-22-34")
                .role(Role.ADMIN)
                .isEnabled(true)
                .isAdmin(true)
                .build();

        AccountEntity mockAccount = AccountEntity.builder()
                .isRentAccount(false)
               // .accountCode("A1")
                .hasMortgage(false)
                .interest(new BigDecimal("2.67"))
                .accountName("Alex Checking")
                .balance(new BigDecimal("1845"))
                .acctID(1)
                .accountType("CHECKING")
                .build();

        DepositsEntity mockDeposit = DepositsEntity.builder()
                .depositID(1)
                //.amount(new BigDecimal("45.00"))
                //  .description("Transfer")
               // .scheduledTime(now)
               // .scheduledDate(today)
                .user(mockUser)
                .account(mockAccount)
                .build();

        List<DepositsEntity> depositsEntities = Collections.singletonList(mockDeposit);

        when(depositService.getDepositsByAcctID(1)).thenReturn(depositsEntities);

        mockMvc.perform(get("/api/deposits/data/{acctID}", acctID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(depositsEntities.size()))
                .andDo(print());

    }



    @AfterEach
    void tearDown() {
    }
}