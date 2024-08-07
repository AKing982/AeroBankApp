package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ConnectionsDTO(Long connectionId,
                             String dbServer,
                             int dbPort,
                             String dbName,
                             String dbUser,
                             String dbPass,
                             String dbUrl,
                             String dbDriver,
                             DBType dbType,
                             LocalDate dateModified) {
}
