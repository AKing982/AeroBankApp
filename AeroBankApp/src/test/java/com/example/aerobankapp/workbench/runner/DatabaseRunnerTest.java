package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.utilities.QueryStatement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DatabaseRunnerTest
{
    @MockBean
    private DatabaseRunner databaseRunner;

    @Mock
    private ResourceLoader resourceLoader;


    private DriverManagerDataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp()
    {
        databaseRunner = new DatabaseRunner(resourceLoader);
        dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("Halflifer94!");

    }

    @Test
    public void testReadSQLStatements() throws Exception
    {
        String expectedSQL = "CREATE TABLE test (id INT);";
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource("src/main/resources/mysqltables.conf")).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(expectedSQL.getBytes(StandardCharsets.UTF_8)));

        String sql = databaseRunner.readSQLStatements("mysqltables.conf");

        assertEquals(expectedSQL, sql, "The readSQLStatements did not return expectedSQL");
    }

    @Test
    void testReadSQLStatementsWithIOException()
    {
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource("src/main/resources/mysqltables.conf")).thenReturn(mockResource);
        try {
            when(mockResource.getInputStream()).thenThrow(new IOException());
            assertThrows(IOException.class, () -> databaseRunner.readSQLStatements("mysqltables.conf"), "Expected IOException was not thrown.");
        } catch (IOException e) {
            // This catch block is not expected to be executed
        }
    }

    @Test
    public void testParseRawSQLToQueryStatementList_Success()
    {
        String rawSQL = "CREATE DATABASE AeroBankApp; CREATE TABLE Users (...); INSERT INTO Users (...) VALUES (...);";
        QueryStatement query1 = new QueryStatement("CREATE DATABASE AeroBankApp");
        QueryStatement query2 = new QueryStatement("CREATE TABLE Users (...)");
        QueryStatement query3 = new QueryStatement("INSERT INTO Users (...) VALUES (...)");
        List<QueryStatement> queryStatementList = Arrays.asList(query1, query2, query3);

        // Act
        List<QueryStatement> actualQueries = databaseRunner.parseRawSQLToQueryStatementList(rawSQL);

        // Assert
        assertEquals(queryStatementList.size(), actualQueries.size());
        assertEquals(queryStatementList.get(0).getQuery(), actualQueries.get(0).getQuery());
        assertEquals(queryStatementList.get(1).getQuery(), actualQueries.get(1).getQuery());
    }

    @Test
    public void testValidateDatabaseNameExists()
    {
        String dbName = "AeroBankApp";
        boolean dbExists = databaseRunner.validateDatabaseNameExists(dbName);

        assertTrue(dbExists);
    }

    @Test
    public void testParseRawSQLToQueryStatementList_LowerCase()
    {
        String badRawSQL = "create database AeroBankApp; create table Users (...); insert into Users (...) values (...);";
        QueryStatement queryStatement = new QueryStatement("CREATE DATABASE AeroBankApp");
        QueryStatement queryStatement1 = new QueryStatement("CREATE TABLE Users (...)");
        QueryStatement queryStatement2 = new QueryStatement("INSERT INTO Users (...) VALUES (...)");
        List<QueryStatement> queryStatementList = List.of(queryStatement, queryStatement1, queryStatement2);

        List<QueryStatement> actualQueries = databaseRunner.parseRawSQLToQueryStatementList(badRawSQL);

        assertEquals(queryStatementList.size(), actualQueries.size());
        assertEquals(queryStatementList.get(0).getQuery(), actualQueries.get(0).getQuery());
        assertEquals(queryStatementList.get(1).getQuery(), actualQueries.get(1).getQuery());
    }

    @Test
    void testExecuteSQLStatements() {
        QueryStatement queryStatement = new QueryStatement("CREATE DATABASE AeroBankApp");
        QueryStatement queryStatement1 = new QueryStatement("CREATE TABLE IF NOT EXISTS Users\n" +
                " (\n" +
                "    userID INT PRIMARY KEY auto_increment,\n" +
                "    userName VARCHAR(225) NOT NULL,\n" +
                "    email VARCHAR(225) NOT NULL,\n" +
                "    password VARCHAR(225) NOT NULL,\n" +
                "    pinNumber CHAR(6) NOT NULL,\n" +
                "    accountNumber VARCHAR(225) NOT NULL,\n" +
                "    role VARCHAR(225) NOT NULL,\n" +
                "    isAdmin BOOL,\n" +
                "    isEnabled BOOL\n" +
                " );");
        QueryStatement queryStatement2 = new QueryStatement("INSERT INTO Users (...) VALUES (...)");
        List<QueryStatement> queryStatements = List.of(queryStatement, queryStatement1, queryStatement2);

        databaseRunner.executeSQLStatements(queryStatements);

        // Verify that jdbcTemplate.execute was called twice, once for each statement
        verify(jdbcTemplate, times(3)).execute(anyString());
    }

    @Test
    void testReadAndExecuteSQLStatements() throws IOException {
        // Setup mock to return a resource for the SQL file

        Resource resource = mock(Resource.class);
        String sqlContent = "CREATE TABLE test1 (id INT); CREATE TABLE test2 (id INT);";
        when(resourceLoader.getResource("src/main/resources/mysqltables.conf")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(sqlContent.getBytes(StandardCharsets.UTF_8)));

        // Simulate reading and executing SQL statements
        String readSqlStatements = databaseRunner.readSQLStatements("mysqltables.conf");
      //  databaseRunner.executeSQLStatements(readSqlStatements);

        // Verify executeSQLStatements method was called with the correct SQL statements
        int expectedStatementCount = sqlContent.split(";").length;
        verify(jdbcTemplate, times(expectedStatementCount)).execute(anyString());

        // Verify countCreateTableStatements method returns the correct count
        int tableCount = databaseRunner.countCreateTableStatements(readSqlStatements);
        assertEquals(2, tableCount, "The count of CREATE TABLE statements should be 2.");
    }

    @Test
    public void testCountCreateTableStatements() {
        DatabaseRunner databaseRunner = new DatabaseRunner(null); // No need for actual dependencies

        // Scenario 1: Multiple CREATE TABLE statements
        String sqlScriptMultiple = "CREATE TABLE users (id INT); CREATE TABLE orders (id INT);";
        int countMultiple = databaseRunner.countCreateTableStatements(sqlScriptMultiple);
        assertEquals(2, countMultiple, "Should count multiple CREATE TABLE statements correctly.");

        // Scenario 2: Case Insensitivity
        String sqlScriptCase = "create table products (id INT); CrEaTe TaBlE categories (id INT);";
        int countCase = databaseRunner.countCreateTableStatements(sqlScriptCase);
        assertEquals(2, countCase, "Should be case insensitive.");

        // Scenario 3: CREATE TABLE within comments
        String sqlScriptComments = "-- CREATE TABLE commented_out (id INT);\nCREATE TABLE active_table (id INT);";
        int countComments = databaseRunner.countCreateTableStatements(sqlScriptComments);
        assertEquals(1, countComments, "Should not count CREATE TABLE within comments.");

        // Scenario 4: No CREATE TABLE statements
        String sqlScriptNone = "SELECT * FROM users; DROP TABLE deprecated;";
        int countNone = databaseRunner.countCreateTableStatements(sqlScriptNone);
        assertEquals(0, countNone, "Should return 0 when no CREATE TABLE statements are present.");
    }





    @AfterEach
    void tearDown() {
    }
}