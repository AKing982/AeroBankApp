package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name="account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int acctID;

    @Column(name="accountCode")
    private String accountCode;

    @Column(name="userID")
    private int userID;

    @Column(name="aSecID")
    private int aSecID;

    @Column(name="accountName")
    private String accountName;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="interest")
    private BigDecimal interest;

    @Column(name="accountType")
    private String accountType;

    @Column(name="hasDividend")
    private boolean hasDividend;

    @Column(name="isRentAccount")
    private boolean isRentAccount;

    @Column(name="hasMortgage")
    private boolean hasMortgage;

    @ManyToMany(mappedBy = "accounts")
    private Set<UserEntity> users;

}
