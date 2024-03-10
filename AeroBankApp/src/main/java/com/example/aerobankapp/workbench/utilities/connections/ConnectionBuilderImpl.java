package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.exceptions.InvalidDBTypeException;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@Getter
public class ConnectionBuilderImpl implements ConnectionBuilder
{
    private final ConnectionRequest connectionRequest;

    @Autowired
    public ConnectionBuilderImpl(ConnectionRequest connectionRequest)
    {
       this.connectionRequest = connectionRequest;
    }

    public DBType getDBType()
    {
        String dbType = getConnectionRequest().getDbType();
        switch(dbType)
        {
            case "MYSQL":
                return DBType.MYSQL;
            case "SSQL":
                return DBType.SSQL;
            case "PSQL":
                return DBType.PSQL;
            default:
                throw new InvalidDBTypeException("Invalid DB Type!");
        }
    }

    public String getDBProtocol() {
        switch (getDBType())
        {
            case MYSQL:
                return "jdbc:mysql";
            case SSQL:
                return "jdbc:sqlserver";
            case PSQL:
                return "jdbc:postgresql";
            default:
                throw new InvalidDBTypeException("Invalid DB Type");
        }
    }

    @Override
    public String getDriverClassName() {
        switch(getDBType())
        {
            case MYSQL:
                return ConnectionParameters.MYSQL_DRIVER;
            case SSQL:
                return ConnectionParameters.SSQL_DRIVER;
            case PSQL:
                return ConnectionParameters.PSQL_DRIVER;
            default:
                throw new InvalidDBTypeException("Invalid Database Type Found.");
        }
    }

    @Override
    public String getUserName() {
        return getConnectionRequest().getDbUser();
    }

    @Override
    public String getPassword()
    {
        return getConnectionRequest().getDbPass();
    }

    @Override
    public String getURL() {
        String host = getConnectionRequest().getDbServer();
        int port = getConnectionRequest().getDbPort();
        String dbName = getConnectionRequest().getDbName();
        switch(getDBType()) {
            case PSQL:
            case MYSQL:
                return String.format("%s://%s:%s/%s", getDBProtocol(), host, port, dbName);
            case SSQL:
                return String.format("%s://%s:%s;databaseName=%s", getDBProtocol(), host, port, dbName);
            default:
                throw new InvalidDBTypeException("Invalid Database Type Found.");
        }
    }

    @Override
    public String getConfigFile() {
        switch(getDBType())
        {
            case PSQL -> {
                return ConnectionParameters.PSQL_CONF;
            }
            case MYSQL -> {
                return ConnectionParameters.MYSQL_CONF;
            }
            case SSQL -> {
                return ConnectionParameters.SSQL_CONF;
            }
            default -> throw new InvalidDBTypeException("Invalid Database Type...");
        }
    }

    @Override
    public DataSource buildDataSource() {
        return null;
    }
}
