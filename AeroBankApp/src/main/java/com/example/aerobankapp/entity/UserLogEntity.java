package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="userLog")
public class UserLogEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity userEntity;

    @NotNull
    @Column(name="lastLogin")
    private LocalDateTime lastLogin;

    @NotNull
    @Column(name="lastLogout")
    private LocalDateTime lastLogout;

    @NotNull
    @Column(name="sessionDuration")
    private int sessionDuration;

    @Column(name="loginSuccess")
    private boolean loginSuccess;

    @Column(name="loginAttempts")
    private int loginAttempts;

    @Column(name="isActive")
    private boolean isActive;
}
