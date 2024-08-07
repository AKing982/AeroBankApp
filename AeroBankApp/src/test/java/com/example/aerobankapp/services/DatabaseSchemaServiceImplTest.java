package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.utilities.connections.ConnectionParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.test.context.support.WithMockUser;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseSchemaServiceImplTest
{
    private DatabaseSchemaService databaseSchemaService;

    private JdbcTemplate jdbcTemplate;

    private String url = "jdbc:mysql://localhost:3306/";

    private String driver = ConnectionParameters.MYSQL_DRIVER;



    @BeforeEach
    void setUp()
    {

    }

    @Test
    @WithMockUser
    public void validateDatabaseNameExists_ValidDatabase()
    {
        final String dbName = "aerobank";
        DataSource dataSource = createDataSource(url + dbName, "root", "Halflifer94!", driver);
        jdbcTemplate = new JdbcTemplate(dataSource);
        databaseSchemaService = new DatabaseSchemaServiceImpl(jdbcTemplate);

        boolean dbExists = databaseSchemaService.validateDatabaseNameExists(dbName);

        assertTrue(dbExists);
    }

    private DataSource createDataSource(String url, String user, String password, String driver)
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @AfterEach
    void tearDown() {
    }
}