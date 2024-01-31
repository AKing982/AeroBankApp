package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.db.DBType;

import java.time.LocalDate;

public record ConnectionsDTO(Long connectionId,
                             String dbServer,
                             String dbPort,
                             String dbName,
                             String dbUser,
                             String dbPass,
                             DBType dbType,
                             LocalDate dateModified) {
}
