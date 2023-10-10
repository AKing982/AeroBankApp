package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="accountNumber")
    private String accountNumber;

    @Column(name="password")
    private char[] password;

    @Column(name="pinNumber")
    private String pinNumber;

    @Column(name="isAdmin")
    private String isAdmin;

    public User(String username, String email, String accountNumber, char[] password, String pinNumber, String isAdmin) {
        this.username = username;
        this.email = email;
        this.accountNumber = accountNumber;
        this.password = password;
        this.pinNumber = pinNumber;
        this.isAdmin = isAdmin;
    }
}
