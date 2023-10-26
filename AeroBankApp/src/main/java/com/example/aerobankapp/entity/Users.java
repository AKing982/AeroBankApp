package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="username")
    @NotNull
    @Size(min=8, message="You must choose at least 8 characters")
    private String username;

    @Column(name="email")
    @NotNull
    private String email;

    @Column(name="accountNumber")
    @NotNull
    private String accountNumber;

    @Column(name="password")
    private char[] password;

    @Column(name="pinNumber")
    @NotNull
    @Size(min=6, message="You must choose at least 6 characters")
    private String pinNumber;

    @Column(name="isAdmin")
    private boolean isAdmin;

    @Column(name="isEnabled")
    private boolean isEnabled;

    @Column(name="isCredentialsNonExpired")
    private boolean isCredentialsNonExpired;

    @Column(name="isAccountNonLocked")
    private boolean isAccountNonLocked;

    @Column(name="isAccountNonExpired")
    private boolean isAccountNonExpired;

    public Users(String username, String email, String accountNumber, char[] password, String pinNumber, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.accountNumber = accountNumber;
        this.password = password;
        this.pinNumber = pinNumber;
        this.isAdmin = isAdmin;
    }
}
