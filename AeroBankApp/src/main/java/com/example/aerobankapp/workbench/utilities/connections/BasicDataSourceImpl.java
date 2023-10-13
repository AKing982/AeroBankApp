package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.Basic;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class BasicDataSourceImpl implements BasicDataSource
{

    private String dbServer;
    private String dbName;
    private int dbPort;
    private String dbProtocol;
    private String dbDriver;
    private String dbURL;
    private String dbUser;
    private String dbPass;
    private DBType dbType;
    private AeroLogger aeroLogger = new AeroLogger(BasicDataSourceImpl.class);

    public BasicDataSourceImpl(BasicDataSourceBuilder builder) {
        this.dbServer = builder.dbServer;
        this.dbName = builder.dbName;
        this.dbPort = builder.dbPort;
        this.dbProtocol = getDBProtocol();
        this.dbDriver = getDBDriver();
        this.dbURL = getDBURL();
        this.dbUser = builder.dbUser;
        this.dbPass = builder.dbPass;
        this.dbType = builder.dbType;
    }

    @Override
    public String getDBDriver()
    {
        String driver = "";
        switch(dbType)
        {
            case MYSQL:
                driver = "com.mysql.cj.jdbc.Driver";
                break;
            case SQLSERVER:
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
            case POSTGRESQL:
                driver = "org.postgresql.Driver";
            default:
                aeroLogger.info("No Such Driver exists in this context");
        }
        dbDriver = driver;
        return dbDriver;

    }

    @Override
    public String getDBName() {
        return dbName;
    }

    @Override
    public String getDBUser() {
        return dbUser;
    }

    @Override
    public String getDBPass() {
        return dbPass;
    }

    @Override
    public String getDBProtocol()
    {
        String protocol = "";
        switch(dbType)
        {
            case MYSQL:
                protocol = "jdbc:mysql";
                break;
            case SQLSERVER:
                protocol = "jdbc:sqlserver";
                break;
            case POSTGRESQL:
                protocol = "jdbc:postgresql";
                break;
        }
        dbProtocol = protocol;
        return dbProtocol;
    }

    @Override
    public String getDBServer()
    {
        return dbServer;
    }

    @Override
    public int getDBPort()
    {
        return dbPort;
    }

    @Override
    public String getDBURL()
    {
        StringBuilder dbURL = null;
        switch(dbType)
        {
            case SQLSERVER:
                dbURL = new StringBuilder();
                dbURL.append(dbProtocol);
                dbURL.append("://");
                dbURL.append(dbServer);
                dbURL.append(":");
                dbURL.append(dbPort);
                dbURL.append(";databaseName=");
                dbURL.append(dbName);
                dbURL.append(";");
                dbURL.append("integratedSecurity=false;");
                dbURL.append("encrypt=true;");
                dbURL.append("trustServerCertificate=true");
                break;
            case MYSQL:
            case POSTGRESQL:
                dbURL = new StringBuilder();
                dbURL.append(dbProtocol);
                dbURL.append("://");
                dbURL.append(dbServer);
                dbURL.append(":");
                dbURL.append(dbPort);
                dbURL.append("/");
                dbURL.append(dbName);
                break;

        }
        return dbURL.toString();
    }

    @Override
    public String toString() {
        return "BasicDataSourceImpl{" +
                "dbServer='" + dbServer + '\'' +
                ", dbName='" + dbName + '\'' +
                ", dbPort=" + dbPort +
                ", dbProtocol='" + dbProtocol + '\'' +
                ", dbDriver='" + dbDriver + '\'' +
                ", dbURL='" + dbURL + '\'' +
                ", dbUser='" + dbUser + '\'' +
                ", dbPass='" + dbPass + '\'' +
                ", dbType=" + dbType +
                '}';
    }

    public static class BasicDataSourceBuilder {
        private String dbServer;
        private String dbName;
        private int dbPort;
        private String dbUser;
        private String dbPass;
        private DBType dbType;

        public BasicDataSourceBuilder setDBServer(String dbServer) {
            this.dbServer = dbServer;
            return this;
        }

        public BasicDataSourceBuilder setDBName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public BasicDataSourceBuilder setDBPort(int port) {
            this.dbPort = port;
            return this;
        }

        public BasicDataSourceBuilder setDBUser(String user) {
            this.dbUser = user;
            return this;
        }

        public BasicDataSourceBuilder setDBPass(String pass)
        {
            this.dbPass = pass;
            return this;
        }

        public BasicDataSourceBuilder setDBType(DBType type)
        {
            this.dbType = type;
            return this;
        }

        public BasicDataSourceImpl build()
        {
            return new BasicDataSourceImpl(this);
        }
    }
}
