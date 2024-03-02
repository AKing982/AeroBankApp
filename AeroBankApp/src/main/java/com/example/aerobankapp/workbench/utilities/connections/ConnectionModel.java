package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ConnectionModel
{
    private String dbServer;
    private int dbPort;
    private String dbName;
    private String dbProtocol;
    private DBType databaseType;
    private String dbUser;
    private String dbPass;
}
