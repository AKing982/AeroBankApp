package com.example.aerobankapp.workbench.utilities.connections;

public interface BasicDataSource
{
    String getDBDriver();
    String getDBProtocol();
    String getDBServer();
    int getDBPort();
    String getDBURL();

}
