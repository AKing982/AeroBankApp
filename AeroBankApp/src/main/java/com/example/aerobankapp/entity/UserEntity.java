package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="account_users",
        joinColumns = @JoinColumn(name="userID"),
    inverseJoinColumns = @JoinColumn(name="acctID"))
    private Set<AccountEntity> accounts;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
