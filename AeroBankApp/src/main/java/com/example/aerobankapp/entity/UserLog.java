package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="userLog")
public class UserLog
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="username")
    @NotNull
    private String username;

    @Column(name="userID")
    private int userID;

    @NotNull
    @Column(name="lastLogin",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date lastLogin;

    public UserLog(String username, int userID, Date lastLogin)
    {
        this.username = username;
        this.userID = userID;
        this.lastLogin = lastLogin;
    }

}
