package com.example.aerobankapp.services;

import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class DatabaseSchemaServiceImpl implements DatabaseSchemaService
{
    private final JdbcTemplate jdbcTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(DatabaseSchemaServiceImpl.class);

    public DatabaseSchemaServiceImpl(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean validateDatabaseNameExists(String dbName) {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";


        // Query for an integer count of database names matching the input name
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{dbName}, Integer.class);

        boolean exists = count != null && count > 0;

        LOGGER.info("Database: " + dbName + (exists ? " exists." : " does not exist."));

        return exists;
    }

    /**
     * Checks if a given table exists in the database.
     *
     * @param tableName The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    @Override
    public boolean validateTableExists(final String tableName)
    {
        String checkTableExistsSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkTableExistsSql, new Object[]{tableName}, Integer.class);
        return count != null && count > 0;
    }

    public boolean testConnection(ConnectionRequest connectionRequest, DataSource dataSource)
    {
        return false;
    }

}
