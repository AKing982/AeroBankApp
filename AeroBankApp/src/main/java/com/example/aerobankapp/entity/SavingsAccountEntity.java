package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="savingsAccount")
public class SavingsAccountEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name="uID")
    private int uID;

    @Column(name="aSecID")
    private int aSecID;

    @Column(name="accountName")
    private String accountName;

    @Column(name="user")
    private String user;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="interestRate")
    private BigDecimal interestRate;

    @Column(name="min_balance")
    private BigDecimal min_balance;

    @Column(name="dividend_amt")
    private BigDecimal dividend_amt;


}
