package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="connections")
public class ConnectionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connectionID;

    @Column(name="dbServer")
    private String dbServer;

    @Column(name="dbPort")
    private String dbPort;

    @Column(name="dbName")
    private String dbName;

    @Column(name="dbUser")
    private String dbUser;

    @Column(name="dbPass")
    private String dbPass;

    @Column(name="dbType")
    @Enumerated(EnumType.STRING)
    private DBType dbType;

    @Column(name="dateModified")
    private LocalDate dateModified;
}
