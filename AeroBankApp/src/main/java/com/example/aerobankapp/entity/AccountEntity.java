package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int acctID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountCodeID")
    @JsonIgnore
    private AccountCodeEntity accountCode;

    @ManyToOne
    @JoinColumn(name="userID")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="aSecID")
    private AccountSecurityEntity accountSecurity;

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
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy="accountEntity")
    @JsonBackReference
    private Set<TransactionStatementEntity> transactions;

    public AccountEntity(int acctID, String accountType)
    {
        this.acctID = acctID;
        this.accountType = accountType;
    }

}
