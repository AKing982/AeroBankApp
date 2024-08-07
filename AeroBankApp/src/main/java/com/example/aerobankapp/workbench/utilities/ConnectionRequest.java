package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
public class ConnectionRequest
{
    private String dbServer;
    private int dbPort;
    private String dbName;
    private String dbUser;
    private String dbPass;
    private String dbType;

    public ConnectionRequest(String dbServer, int dbPort, String dbName, String dbUser, String dbPass, String dbType) {
        this.dbServer = dbServer;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.dbType = dbType;
    }

    public ConnectionRequest()
    {

    }

    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
