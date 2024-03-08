package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.services.DatabaseSchemaService;
import com.example.aerobankapp.services.DatabaseSchemaServiceImpl;
import com.example.aerobankapp.workbench.utilities.QueryStatement;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionBuilder;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionModel;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Setter
@Getter
public class DatabaseRunner
{
    private final ResourceLoader resourceLoader;
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ConnectionModel connectionModel;
    private ConnectionBuilder connectionBuilder;
    private static final String resourceString = "classpath:conf/";
    private static final String createTableSQL = "CREATE TABLE";
    private static final String createDatabaseSQL = "CREATE DATABASE";
    private static final String insertSQL = "INSERT INTO";
    private static final String SQLFILE = "mysqltables.conf";
    private Logger LOGGER = LoggerFactory.getLogger(DatabaseRunner.class);
    private static long startTime;
    private static long endTime;

    @Autowired
    public DatabaseRunner(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    public void setConnectionModel(final String server, final int port, final String dbName, final String dbUser, final String dbPass, final DBType type)
    {
        this.connectionModel.setDbServer(server);
        this.connectionModel.setDbPort(port);
        this.connectionModel.setDbName(dbName);
        this.connectionModel.setDbUser(dbUser);
        this.connectionModel.setDbPass(dbPass);
        this.connectionModel.setDatabaseType(type);
    }

    public void setJdbcTemplate(final DriverManagerDataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createDataSource(final String server, final int port, final String dbName, final String user, final String password, final DBType type, boolean includeDBName)
    {
        final String url = includeDBName ? getDatabaseURL(type, server, port, dbName) : getServerURL(type, server, port);
        DriverManagerDataSource dataSource = getDriverDataSource(url, user, password, type);
        setJdbcTemplate(dataSource);
    }

    private DriverManagerDataSource getDriverDataSource(final String url, final String user, final String pass, final DBType type)
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, user, pass);
        dataSource.setDriverClassName(getDriverClassName(type));
        return dataSource;
    }

    public void configureDataSourceForNewDatabase(final String dbName) {
        // Assumes connectionModel or similar approach holds the latest connection info
        final String server = this.connectionModel.getDbServer();
        LOGGER.info("Server: " + server);
        final int port = this.connectionModel.getDbPort();
        LOGGER.info("Port: " + port);
        final String user = this.connectionModel.getDbUser();
        LOGGER.info("User: " + user);
        final String password = this.connectionModel.getDbPass();
        LOGGER.info("Password: " + password);
        final DBType type = this.connectionModel.getDatabaseType();
        LOGGER.info("DBType: " + type);

        // Include the dbName in the connection URL
        final String url = getDatabaseURL(type, server, port, dbName);
        DriverManagerDataSource dataSource = getDriverDataSource(url, user, password, type);

        // Update jdbcTemplate and namedParameterJdbcTemplate to use the new DataSource
        setJdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private String getConfigFile(DBType type)
    {
        switch(type)
        {
            case SSQL -> {
                return "ssqltables.conf";
            }
            case MYSQL -> {
                return "mysqltables.conf";
            }
            case PSQL -> {
                return "psqltables.conf";
            }
            default -> throw new IllegalArgumentException("Invalid Database Type...");
        }
    }

    public void createNewDatabaseWithTables() throws IOException {

        String dbName = getConnectionModel().getDbName();
        DBType type = getConnectionModel().getDatabaseType();
        String configFile = getConfigFile(type);

        // Check if the database already exists
        boolean databaseExists = validateDatabaseNameExists(dbName);
        if(!databaseExists)
        {
            configureDataSourceForNewDatabase(dbName);
            createDatabase(dbName);
        }

        // Get the CREATE Table statements
        String tableStatements = readSQLStatements(configFile);

        // Parse Raw SQL Statement
        List<QueryStatement> queryStatements = parseRawSQLToQueryStatementList(tableStatements);

        // Execute the Table scripts
        executeSQLStatements(queryStatements);
    }

    public void createDatabase(String dbName)
    {
        getJdbcTemplate().execute(String.format("CREATE DATABASE %s", dbName));
        getJdbcTemplate().execute(String.format("USE %s", dbName));
    }

    private String getServerURL(DBType dbType, String server, int port) {
        switch (dbType)
        {
            case MYSQL -> {
                return String.format("%s://%s:%s", "jdbc:mysql", server, port);
            }
            case SSQL -> {
                return String.format("%s://%s:%s", "jdbc:sqlserver", server, port);
            }
            default -> throw new IllegalArgumentException("Invalid Database Type...");
        }
    }

    private String getDatabaseURL(DBType dbType, String server, int port, String dbName)
    {
        switch(dbType)
        {
            case SSQL -> {
                return String.format("%s://%s:%s;databaseName=%s", "jdbc:sqlserver", server, port, dbName);
            }
            case MYSQL ->{
                return String.format("%s://%s:%s/%s", "jdbc:mysql",server, port, dbName);
            }
            case PSQL -> {
                return String.format("%s://%s:%s/%s", "jdbc:postgresql", server, port, dbName);
            }
            default -> throw new IllegalArgumentException("Invalid parameters entered...");
        }
    }

    private String getDriverClassName(DBType dbType)
    {
        switch(dbType)
        {
            case PSQL -> {
                return "org.postgresql.driver";
            }
            case MYSQL -> {
                return "com.mysql.cj.jdbc.Driver";
            }
            case SSQL -> {
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            }
            default -> throw new IllegalArgumentException("Invalid Database type found...");
        }
    }

    public void connectToDatabase()
    {
        try
        {
            DriverManagerDataSource driverManagerDataSource = buildDriverDataSource(getConnectionModel());
            // Set the JDBC template
            this.jdbcTemplate = new JdbcTemplate(driverManagerDataSource);
            LOGGER.info("Successfully connected to database: " + getConnectionModel().getDbName());

        }catch(Exception e)
        {
            LOGGER.error("Connection Failed: " + e.getMessage());
        }
    }

    private DriverManagerDataSource buildDriverDataSource(final ConnectionModel connectionModel)
    {
        final String server = getConnectionModel().getDbServer();
        final int port = getConnectionModel().getDbPort();
        final String dbName = getConnectionModel().getDbName();
        final String dbPass = getConnectionModel().getDbPass();
        final String dbUser = getConnectionModel().getDbUser();
        final DBType type = getConnectionModel().getDatabaseType();
        final String driver = getConnectionModel().getDriver();

        // Create the URL
        final String url = getDatabaseURL(type, server, port, dbName);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);
        dataSource.setUrl(url);
        return dataSource;
    }

    public String readSQLStatements(String fileName) throws IOException {
        final String filePath = resourceString + fileName;
        var resource = getResourcePath(filePath);
        try(var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)))
        {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public Resource getResourcePath(String path)
    {
        return resourceLoader.getResource(path);
    }

    public int countCreateTableStatements(String sqlScript)
    {
        String[] lines = sqlScript.split(";");
        int count = 0;
        for(String line : lines)
        {
            if(line.trim().toUpperCase().startsWith(createTableSQL))
            {
                count++;
            }
        }
        return count;
    }

    public List<QueryStatement> parseRawSQLToQueryStatementList(final String rawSQL)
    {
        List<QueryStatement> queryStatementList = new ArrayList<>();
        String[] statements = rawSQL.split(";");
        for(String statement : statements) {
            // Trim the statement
            String trimmedStatement = statement.trim();

            // Is the statement in all lowercase?
            String processedStatement = capitalizeSqlKeywords(trimmedStatement);

            processAndAddStatement(queryStatementList, processedStatement);
        }
        return queryStatementList;
    }

    private String capitalizeSqlKeywords(String sqlStatement) {
        if (sqlStatement.toLowerCase().startsWith("create database")) {
            return createDatabaseSQL + sqlStatement.substring(15);
        } else if (sqlStatement.toLowerCase().startsWith("insert into")) {
            return insertSQL + sqlStatement.substring(11);
        } else if (sqlStatement.toLowerCase().startsWith("create table")) {
            return createTableSQL + sqlStatement.substring(12);
        }
        return sqlStatement; // Return original if no match
    }

    public void processAndAddStatement(List<QueryStatement> queryStatements, String sqlStatement)
    {
        QueryStatement queryStatement = createQueryStatement(sqlStatement);
        logQueryStatement(queryStatement);
        addQueryToList(queryStatements, queryStatement);
    }

    public void addQueryToList(List<QueryStatement> queryStatements, QueryStatement statement)
    {
        queryStatements.add(statement);
    }

    private QueryStatement createQueryStatement(final String query)
    {
        return new QueryStatement(query);
    }

    private void logQueryStatement(QueryStatement queryStatement)
    {
        LOGGER.info("Query Statement: " + queryStatement.getQuery());
    }

    public void executeSQLStatements(final List<QueryStatement> statements)
    {
        // Initialize the JdbcTemplate if its not already initialized
        startTime = System.nanoTime();
        int queryCount = 0;
        if(!statements.isEmpty())
        {
            for(QueryStatement statement : statements)
            {
                String query = statement.getQuery();
                if(query.isEmpty())
                {
                    // Skip the empty query
                    continue;
                }
                LOGGER.info("Executing Query: " + query);
                queryCount++;
                getJdbcTemplate().execute(query);
            }
        }

        endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        LOGGER.info("Total Time Executed: " + duration + " ms");
        LOGGER.info("Total Queries Executed: " + queryCount);
    }

    private void setJdbcDataSource(DataSource dataSource)
    {
        jdbcTemplate.setDataSource(dataSource);
    }

    //TODO: Add Implementation
    public void updateExistingDatabaseTables()
    {
        // Get the Database Name
        String dbName = getConnectionModel().getDbName();

        // Does the existing database exist?
        if(validateDatabaseNameExists(dbName))
        {
            // Connect to the database
            connectToDatabase();
        }
    }

    public void addNewTablesToDatabase()
    {

    }


    public boolean validateDatabaseNameExists(final String dbName)
    {
        // Check if the DataSource is null, otherwise connect the dataSource to localhost
        DataSource dataSource = getJdbcTemplate().getDataSource();
        if(dataSource == null)
        {
            LOGGER.info("Data Source is NULL");

            // Set the DataSource to use the default localhost
            setDefaultDataSource(getJdbcTemplate(), dbName);
        }
        LOGGER.info("Database: " + dbName);
        DatabaseSchemaService databaseSchemaService = new DatabaseSchemaServiceImpl(getJdbcTemplate());
        LOGGER.info("Database is Valid: " + databaseSchemaService.validateDatabaseNameExists(dbName));
        return databaseSchemaService.validateDatabaseNameExists(dbName);
    }

    private void setDefaultDataSource(JdbcTemplate jdbcTemplate, String dbName)
    {
        final String url = "jdbc:mysql://localhost:3306/" + dbName.trim();
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setPassword("Halflifer94!");
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setUrl(url);

        getJdbcTemplate().setDataSource(driverManagerDataSource);
    }

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(DatabaseRunner.class, args);

        DatabaseRunner runner = context.getBean(DatabaseRunner.class);

        final String server = "localhost";
        final int port = 3306; // Example for MySQL
        final String dbName = "testDB";
        final String user = "root";
        final String password = "Halflifer94!";
        final DBType type = DBType.MYSQL;
        final String configFile = "mysqltables.conf";
        final String driver = "com.mysql.cj.jdbc.Driver";

        ConnectionModel connectionModel = new ConnectionModel();
        connectionModel.setDbProtocol("jdbc:mysql");
        connectionModel.setDbPort(port);
        connectionModel.setDbName(dbName);
        connectionModel.setDbUser(user);
        connectionModel.setDbPass(password);
        connectionModel.setDatabaseType(type);
        connectionModel.setDbServer(server);
        connectionModel.setDriver(driver);

        runner.setConnectionModel(connectionModel);

        // Setup DataSource and initialize database and tables
        // Now it's safe to proceed with database and tables creation
        runner.setupAndInitializeDatabase(server, port, dbName, user, password, type);

        context.close();
    }

    // Additional helper method to encapsulate DataSource setup and database/table initialization
    public void setupAndInitializeDatabase(String server, int port, String dbName, String user, String password, DBType type) {
        LOGGER.info("Initializing Database Creation");
        try {
            if (!validateDatabaseNameExists(dbName)) {
                LOGGER.info("Create Data Source");
                createDataSource(server, port, "", user, password, type, false);
                createDatabase(dbName);
                // Reconfigure DataSource to point to the newly created database
            }
            createDataSource(server, port, dbName, user, password, type, true);
            createNewDatabaseWithTables();

        } catch (IOException e) {
            LOGGER.error("Failed to create the database and tables.", e);
        }
    }

}
