package com.example.aerobankapp.controllers;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JacksonConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.configuration.RabbitMQConfig;
import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.services.TransferService;
import com.example.aerobankapp.workbench.utilities.TransferType;
import com.example.aerobankapp.workbench.utilities.conversion.TransferMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(value=TransferController.class, excludeAutoConfiguration= SecurityAutoConfiguration.class)
@Import({AppConfig.class, JpaConfig.class, JacksonConfig.class})
@RunWith(SpringRunner.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private TransferMapper transferMapper;

    @Mock
    private TransferDTO mockTransferDTO;

    @Autowired
    @Qualifier("jacksonObjectMapper")
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void testSaveEndPoint_TransferDTO_Null_returnBadRequest() throws Exception {

        String jsonString = objectMapper.writeValueAsString(null);

        mockMvc.perform(post("/api/transfers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testSaveEndPoint_TransferDTO_NonNull_returnStatus200() throws Exception {
        LocalDate transferDate = LocalDate.of(2024, 7, 14);
        LocalTime transferTime = LocalTime.of(12, 0);
        boolean notificationEnabled = true;
        LocalDate dateTransferred = LocalDate.of(2024, 7, 15);
        TransferDTO transferDTO = new TransferDTO(1L, 1, 1, 1, 2, BigDecimal.TEN, "Test", transferDate, transferTime, TransferType.USER_TO_USER, notificationEnabled, dateTransferred);
        String jsonString = objectMapper.writeValueAsString(transferDTO);

        mockMvc.perform(post("/api/transfers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @AfterEach
    void tearDown() {
    }
}