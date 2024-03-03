package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
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
}
