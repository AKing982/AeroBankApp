package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.services.ConnectionsService;
import com.example.aerobankapp.workbench.utilities.db.DBType;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = ConnectionsController.class)
@RunWith(SpringRunner.class)
class ConnectionsControllerTest {

    @MockBean
    @Qualifier("connectionsServiceImpl")
    private ConnectionsService connectionsService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void getListOfConnections() throws Exception {
        ConnectionsEntity connectionsEntity = ConnectionsEntity.builder()
                .connectionID(1L)
                .dbName("aerobank")
                .dbServer("localhost")
                .dbPort("3306")
                .dbPass("Halflifer94!")
                .dbType(DBType.MYSQL)
                .dbUser("root")
                .build();

        List<ConnectionsEntity> connections = Collections.singletonList(connectionsEntity);

        given(connectionsService.findAll()).willReturn(connections);

        mockMvc.perform(get("/api/connections/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());


    }

    @AfterEach
    void tearDown() {
    }
}