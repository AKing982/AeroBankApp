package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @Column(name="externalAcctID")
    private String externalAcctID;

    @Column(name="externalId")
    @NotNull
    @NotBlank
    private String externalId;

    @Column(name="amount")
    private BigDecimal amount;

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

    @Column(name="createdAt")
    private LocalDateTime createdAt;


    public PlaidTransactionEntity() {

    }
}
