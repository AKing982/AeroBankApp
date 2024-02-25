package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.exceptions.InvalidDBTypeException;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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
            case "MySQL":
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
                return "com.mysql.cj.jdbc.Driver";
            case SSQL:
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case PSQL:
                return "org.postgresql.Driver";
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
        String host = connectionRequest.getDbServer();
        int port = connectionRequest.getDbPort();
        String dbName = connectionRequest.getDbName();
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
}
