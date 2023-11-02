package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name="userLog")
public class UserLog
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="username")
    @NotNull
    private String username;

    @Column(name="userID")
    private int userID;

    @Column(name="lastLogin")
    @NotNull
    private Date lastLogin;

    public UserLog(String username, int userID, Date lastLogin)
    {
        this.username = username;
        this.userID = userID;
        this.lastLogin = lastLogin;
    }

}
