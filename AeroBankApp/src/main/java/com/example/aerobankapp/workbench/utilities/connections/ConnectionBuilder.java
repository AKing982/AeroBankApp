package com.example.aerobankapp.workbench.utilities.connections;

import javax.sql.DataSource;

public interface ConnectionBuilder
{
   String getDriverClassName();
   String getUserName();
   String getPassword();
   String getURL();
   String getConfigFile();
   DataSource buildDataSource();
}
