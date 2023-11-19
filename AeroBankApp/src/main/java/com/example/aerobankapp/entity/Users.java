package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username")
    @NotNull
    @Size(min=8, message="You must choose at least 8 characters")
    private String username;

    @Column(name="email")
    @NotNull
    private String email;

    @Column(name="password")
    @Size(min=24, max=225, message="You must choose atleast 25 characters")
    private String password;

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

}
