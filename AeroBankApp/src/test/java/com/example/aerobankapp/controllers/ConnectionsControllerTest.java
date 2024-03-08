package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.services.ConnectionsService;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = ConnectionsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
class ConnectionsControllerTest {

    @MockBean
    @Qualifier("connectionsServiceImpl")
    private ConnectionsService connectionsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .dbPort(3306)
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

    @Test
    @WithMockUser
    public void testConnect_whenConnectionIsValid_returnOk() throws Exception
    {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setDbName("aerobank");
        connectionRequest.setDbUser("root");
        connectionRequest.setDbPort(3306);
        connectionRequest.setDbPass("Halflifer94!");
        connectionRequest.setDbServer("localhost");
        connectionRequest.setDbType("MYSQL");

        String objectMappedStr = objectMapper.writeValueAsString(connectionRequest);
        System.out.println(objectMappedStr.toString());

        mockMvc.perform(post("/api/connections/connect")
                .content(objectMappedStr)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Created"));
    }

    @Test
    @WithMockUser
    public void checkDatabaseNameExists_whenDatabaseExists_returnOk() throws Exception
    {
        // Arrange
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setDbName("aerobank");
        connectionRequest.setDbUser("root");
        connectionRequest.setDbPort(3306);
        connectionRequest.setDbPass("Halflifer94!");
        connectionRequest.setDbServer("localhost");
        connectionRequest.setDbType("MYSQL");

        when(connectionsService.databaseNameExists(any(ConnectionRequest.class))).thenReturn(true);

        mockMvc.perform(post("/api/connections/database/validateName")
                                .content(objectMapper.writeValueAsString(connectionRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Database: " + connectionRequest.getDbName() + " Exists"));
    }

    @Test
    @WithMockUser
    public void checkDatabaseNameExists_whenDatabaseDoesNotExist_returnsNotFound() throws Exception {
        // Given
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setDbName("noSuchDB");
        connectionRequest.setDbUser("root");
        connectionRequest.setDbPort(3306);
        connectionRequest.setDbPass("Halflifer94!");
        connectionRequest.setDbServer("localhost");
        connectionRequest.setDbType("MYSQL");

        when(connectionsService.databaseNameExists(any(ConnectionRequest.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/connections/database/validateName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectionRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Database: noSuchDB Not Found."));
    }



    @AfterEach
    void tearDown() {
    }
}