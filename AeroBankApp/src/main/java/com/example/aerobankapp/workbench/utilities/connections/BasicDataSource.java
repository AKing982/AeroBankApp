package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.workbench.utilities.db.DBType;

@Deprecated
public interface BasicDataSource
{
    String getDBDriver();
    String getDBName();
    String getDBUser();
    String getDBPass();
    String getDBProtocol();
    String getDBServer();
    int getDBPort();
    String getDBURL();

    DBType getDBType();

}
