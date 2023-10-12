package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Component
public class ConnectionDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String dbName;
    private String dbServer;
    private String dbDriver;
    private int dbPort;
    private String dbUser;
    private String dbPass;
    private String dateModified;
    private DBType dbType;

    public ConnectionDTO(ConnectionDTOBuilder builder)
    {
        this.dbName = builder.dbName;
        this.dbServer = builder.dbServer;
        this.dbDriver = builder.dbDriver;
        this.dbPort = builder.dbPort;
        this.dbUser = builder.dbUser;
        this.dbPass = builder.dbPass;
        this.dateModified = builder.dateModified;
        this.dbType = builder.dbType;
    }

    public static class ConnectionDTOBuilder
    {
        private String dbName;
        private String dbServer;
        private String dbDriver;
        private int dbPort;
        private String dbUser;
        private String dbPass;
        private String dateModified;
        private DBType dbType;

        public ConnectionDTOBuilder setDBName(final String name)
        {
            this.dbName = name;
            return this;
        }

        public ConnectionDTOBuilder setDBServer(final String server)
        {
            this.dbServer = server;
            return this;
        }

        public ConnectionDTOBuilder setDBDriver(final String driver)
        {
            this.dbDriver = driver;
            return this;
        }

        public ConnectionDTOBuilder setDBPort(final int port)
        {
            this.dbPort = port;
            return this;
        }

        public ConnectionDTOBuilder setDBUser(final String user)
        {
            this.dbUser = user;
            return this;
        }

        public ConnectionDTOBuilder setDBPassword(final String pass)
        {
            this.dbPass = pass;
            return this;
        }

        public ConnectionDTOBuilder setDateModified(String dateModified)
        {
            this.dateModified = dateModified;
            return this;
        }

        public ConnectionDTOBuilder setDBType(DBType type)
        {
            this.dbType = type;
            return this;
        }

        public ConnectionDTO build()
        {
            return new ConnectionDTO(this);
        }
    }
}
