package com.example.aerobankapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;


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
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        try {
            String result = jdbcTemplate.queryForObject(sql, new Object[]{dbName}, String.class);
            LOGGER.info("DatabaseName: " + result);
            LOGGER.info("DatabaseName == " + dbName + ": " + dbName.equals(result));
            return dbName.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
}
