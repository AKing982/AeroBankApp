package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="plaidTransactions")
@Data
public class PlaidTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountId")
    private AccountEntity account;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="location")
    private String location;

    @Column(name="name")
    private String name;

    @Column(name="pending")
    private boolean pending;

    @Column(name="date")
    private LocalDate date;

    @Column(name="merchantName")
    private String merchantName;

    @Column(name="authorizedDate")
    private LocalDate authorizedDate;


    public PlaidTransactionEntity() {

    }
}
