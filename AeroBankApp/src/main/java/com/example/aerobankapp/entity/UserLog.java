package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Size(min=10, max=25, message="Username requires atleast 10 characters")
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
