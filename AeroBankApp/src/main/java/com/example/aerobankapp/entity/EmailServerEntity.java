package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="EmailServer")
public class EmailServerEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="host")
    private String host;

    @Column(name="port")
    @Max(60751)
    @Min(1)
    private int port;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="isTLS")
    private boolean isTLS;
}
