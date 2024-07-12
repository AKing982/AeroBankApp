package com.example.aerobankapp.controllers;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
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
import org.springframework.context.annotation.Import;
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
@Import({JpaConfig.class, AppConfig.class})
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




    @AfterEach
    void tearDown() {
    }
}