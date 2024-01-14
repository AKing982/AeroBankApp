package com.example.aerobankapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="investmentAccount")
@Deprecated
public class InvestmentAccountEntity
{

    @Id
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
