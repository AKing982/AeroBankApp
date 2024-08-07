package com.example.aerobankapp.services;

import com.example.aerobankapp.DatabaseProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class DatabasePropsService
{
    private DatabaseProperties databaseProperties;

    @Autowired
    public DatabasePropsService(DatabaseProperties databaseProperties)
    {
        this.databaseProperties = databaseProperties;
    }

    public void updateDatabaseConnection(String url, String user, String pass)
    {
        getDatabaseProperties().setUrl(url);
        getDatabaseProperties().setDbPass(pass);
        getDatabaseProperties().setDbUser(user);
    }
}
