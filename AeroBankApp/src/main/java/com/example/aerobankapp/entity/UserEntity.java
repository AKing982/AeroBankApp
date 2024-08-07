package com.example.aerobankapp.entity;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.workbench.utilities.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Table(name="users")
@Access(AccessType.FIELD)
@Data
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Embedded
    private UserDetails userDetails;

    @Embedded
    private UserSecurity userSecurity;

    @Embedded
    private UserCredentials userCredentials;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<UserLogEntity> userLogs;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="account_users",
        joinColumns = @JoinColumn(name="userID"),
    inverseJoinColumns = @JoinColumn(name="acctID"))
    private Set<AccountEntity> accounts = new HashSet<>();

    public UserEntity(int userID, UserDetails userDetails, UserSecurity userSecurity, UserCredentials userCredentials) {
        this.userID = userID;
        this.userDetails = userDetails;
        this.userSecurity = userSecurity;
        this.userCredentials = userCredentials;
    }

    public UserEntity(int userID, UserDetails userDetails, UserSecurity userSecurity, UserCredentials userCredentials, Set<UserLogEntity> userLogs, Set<AccountEntity> accounts) {
        this.userID = userID;
        this.userDetails = userDetails;
        this.userSecurity = userSecurity;
        this.userCredentials = userCredentials;
        this.userLogs = userLogs;
        this.accounts = accounts;
    }

    public UserEntity(int userID, UserDetails userDetails, UserSecurity userSecurity, UserCredentials userCredentials, Set<AccountEntity> accounts) {
        this.userID = userID;
        this.userDetails = userDetails;
        this.userSecurity = userSecurity;
        this.userCredentials = userCredentials;
        this.accounts = accounts;
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
    public int hashCode(){
        return Objects.hash(userID);
    }

}
