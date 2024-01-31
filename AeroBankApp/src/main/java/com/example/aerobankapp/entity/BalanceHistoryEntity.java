package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Table(name="balanceHistory")
public class BalanceHistoryEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyID;

    @ManyToOne
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name="transactionID")
    private TransactionEntity transaction;

    @ManyToOne
    @JoinColumn(name="accountDetailsID")
    private AccountDetailsEntity accountDetails;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="adjusted")
    private BigDecimal adjusted;

    @Column(name="lastBalance")
    private BigDecimal lastBalance;

    @Column(name="transactionType")
    private String transactionType;

    @Column(name="createdBy")
    private String createdBy;

    @Column(name="createdAt")
    private LocalDateTime createdAt;

    @Column(name="updatedBy")
    private String updatedBy;

    @Column(name="currency")
    private String currency;

    @Column(name="posted")
    private LocalDate posted;
}
