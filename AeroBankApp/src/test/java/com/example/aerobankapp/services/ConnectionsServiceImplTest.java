package com.example.aerobankapp.services;

import com.example.aerobankapp.DatabaseInitializer;
import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.repositories.ConnectionRepository;
import com.example.aerobankapp.workbench.runner.DatabaseRunner;
import com.example.aerobankapp.workbench.utilities.DataSourceProperties;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionModel;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ConnectionsServiceImplTest
{
    @Autowired
    private ConnectionsServiceImpl connectionsService;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DatabaseRunner databaseRunner;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Mock
    private EntityManager entityManager;


    @BeforeEach
    void setUp() {
    }

    @Test
    public void testFindAll()
    {

        LocalDate now = LocalDate.now();
        ConnectionsEntity connectionsEntity = ConnectionsEntity.builder()
                .connectionID(1L)
                .dbType(DBType.MYSQL)
                .dbUser("root")
                .dbPass("Halflifer94!")
                .dbPort(3306)
                .dbServer("localhost")
                .dateModified(now)
                .dbName("aerobank")
                .build();

        List<ConnectionsEntity> expectedList = Collections.singletonList(connectionsEntity);
        List<ConnectionsEntity> connectionsEntityList = connectionsService.findAll();

        assertNotNull(connectionsEntityList);
        assertEquals(1, connectionsEntityList.size());;
        assertEquals(connectionsEntityList.get(0).getConnectionID(), connectionsEntity.getConnectionID());
    }

    @Test
    public void testGetConnectionById()
    {
        LocalDate now = LocalDate.now();
        ConnectionsEntity connectionsEntity = ConnectionsEntity.builder()
                .connectionID(1L)
                .dbType(DBType.MYSQL)
                .dbUser("root")
                .dbPass("Halflifer94!")
                .dbPort(3306)
                .dbServer("localhost")
                .dateModified(now)
                .dbName("aerobank")
                .build();

        Long connectionID = 1L;
        ConnectionsEntity actualConnectionsEntity = connectionsService.getConnectionById(connectionID);

        assertNotNull(actualConnectionsEntity);
        assertNotEquals(connectionsEntity, actualConnectionsEntity);
        assertEquals(connectionsEntity.getConnectionID(), actualConnectionsEntity.getConnectionID());
    }

    @Test
    public void testGetServerAddressById()
    {
        String expectedAddress = "localhost";

        String actualAddress = connectionsService.getServerAddressById(1L);

        assertNotNull(actualAddress);
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void testGetPortById()
    {
        int port = 3306;

        int actualPort = connectionsService.getPortById(1L);

        assertNotNull(actualPort);
        assertEquals(port, actualPort);
    }

    @Test
    public void testGetUserNameById()
    {
        String user = "root";

        String actualUser = connectionsService.getUserNameById(1L);

        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test
    void createDatabaseFailureTest() throws IOException {

        ConnectionModel connectionModel = new ConnectionModel();
        connectionModel.setDatabaseType(DBType.MYSQL);
        connectionModel.setDbPort(3306);
        connectionModel.setDbUser("root");
        connectionModel.setDbPass("Halflifer94!");
        connectionModel.setDbName("testDB");
        connectionModel.setDbServer("localhost");

        databaseRunner.setConnectionModel(connectionModel);

        // Simulate an exception when creating a new database with tables

        // Assert that an exception is thrown when calling createDatabase
        assertThrows(RuntimeException.class, () -> databaseRunner.createDatabase("testDB"),
                "Expected createDatabase to throw, but it didn't");
    }

    @Test
    void testCreateDatabaseCallsMainMethod() {

        // Mock the DatabaseInitializer

        ConnectionsEntity connectionModel = new ConnectionsEntity();
        connectionModel.setDbType(DBType.MYSQL);
        connectionModel.setDbPort(3306);
        connectionModel.setDbUser("root");
        connectionModel.setDbPass("Halflifer94!");
        connectionModel.setDbName("testDB");
        connectionModel.setDbServer("localhost");

        // Call the method under test
        connectionsService.createDatabase(connectionModel);

        // Verify that initialize was called with the expected arguments
        verify(databaseInitializer, times(1)).initialize(any(String[].class));
    }

    @AfterEach
    void tearDown() {
    }
}