package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(value= DepositControllerTest.class)
@RunWith(SpringRunner.class)
class DepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositController depositController;

    @BeforeEach
    void setUp() {
    }



    @Test
    public void whenDepositIsPosted_ThenReturnDepositResponse() throws Exception
    {
        DepositRequest depositDTO = new DepositRequest("A1", "1414", "01-30-24", "11:50 AM", "Once", "Transfer");

        mockMvc.perform(post("/api/deposits/create")
                .content(String.valueOf(depositDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountCode").value("A1"))
                .andDo(print());
    }


    @AfterEach
    void tearDown() {
    }
}