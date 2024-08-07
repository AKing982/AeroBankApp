package com.example.aerobankapp.services;

public interface DatabaseSchemaService
{
    boolean validateDatabaseNameExists(final String dbName);
    boolean validateTableExists(String tableName);

}
