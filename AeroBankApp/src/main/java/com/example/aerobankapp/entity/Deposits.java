package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="deposits")
@NoArgsConstructor
@AllArgsConstructor
public class Deposits
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int depositID;

    @Column(name="userID")
    private int userID;

    @Column(name="accountID")
    private String acctID;

    @Column(name="accountName")
    private String accountName;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="isDebit")
    private boolean isDebit;

    @Column(name="isBankTransfer")
    private boolean isBankTransfer;

    @Column(name="date_posted")
    private LocalDate date_posted;


}
