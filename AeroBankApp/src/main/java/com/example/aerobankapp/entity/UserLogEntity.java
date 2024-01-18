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

    @Column(name="username")
    @Size(min=10, max=25, message="Username requires atleast 10 characters")
    @NotNull
    private String username;

    @Column(name="userID")
    private int userID;

    @NotNull
    @Column(name="lastLogin",nullable = false)
    private LocalDateTime lastLogin;

    public UserLogEntity(String username, int userID, LocalDateTime lastLogin)
    {
        this.username = username;
        this.userID = userID;
        this.lastLogin = lastLogin;
    }

}
