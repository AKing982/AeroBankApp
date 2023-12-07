package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="purchases")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseEntity
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="accountID")
    private String accountID;

    @Column(name="userID")
    private int userID;

    @Column(name="user")
    private String user;

    @Column(name="accountName")
    @NotNull
    private String accountName;

    @Column(name="cardNumber")
    @NotNull
    private String cardNo;

    @Column(name="amount")
    @NotNull
    private BigDecimal amount;

    @Column(name="description")
    @NotNull
    private String description;

    @Column(name="date_posted")
    private LocalDate date_posted;

    @Column(name="isProcessed")
    private boolean isProcessed;

    @Column(name="isDebit")
    private boolean isDebit;
}
