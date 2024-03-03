package com.example.aerobankapp.workbench.utilities.connections;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Component
public class ConnectionModel
{
    private String dbServer;
    private int dbPort;
    private String dbName;
    private String dbProtocol;
    private DBType databaseType;
    private String dbUser;
    private String dbPass;
    private String driver;
}
