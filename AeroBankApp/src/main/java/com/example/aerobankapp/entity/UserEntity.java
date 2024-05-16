package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

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

    @Column(name="accountNumber")
    @NotNull
    private String accountNumber;

    @Column(name="isAdmin")
    private boolean isAdmin;

    @Column(name="isEnabled")
    private boolean isEnabled;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="account_users",
        joinColumns = @JoinColumn(name="userID"),
    inverseJoinColumns = @JoinColumn(name="acctID"))
    private Set<AccountEntity> accounts = new HashSet<>();

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="profileImgUrl")
    private String profileImgUrl;

    public UserEntity(String firstName, String lastName, String username, String email, String password, String pinNumber, String accountNumber, boolean isAdmin, boolean isEnabled, Set<AccountEntity> accounts, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.accountNumber = accountNumber;
        this.isAdmin = isAdmin;
        this.isEnabled = isEnabled;
        this.accounts = accounts;
        this.role = role;
    }

    public void addAccount(AccountEntity accountEntity)
    {
        accounts.add(accountEntity);
        accountEntity.getUsers().add(this);
    }

    public void removeAccount(AccountEntity accountEntity)
    {
        accounts.remove(accountEntity);
        accountEntity.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pinNumber='" + pinNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", isAdmin=" + isAdmin +
                ", isEnabled=" + isEnabled +
                ", accounts=" + accounts +
                ", role=" + role +
                '}';
    }
}
