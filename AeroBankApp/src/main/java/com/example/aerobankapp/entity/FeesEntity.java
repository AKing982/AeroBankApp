package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="fees")
@AllArgsConstructor
@NoArgsConstructor
public class FeesEntity
{
    @Id
    private String acctID;

    @Column(name="standardFees")
    private BigDecimal standardFees;

    @Column(name="annualFees")
    private BigDecimal annualFees;

    @Column(name="lateFees")
    private BigDecimal lateFees;

    @Column(name="transactionFees")
    private BigDecimal transactionFees;

    @Column(name="earlyWithdrawalFee")
    private BigDecimal earlyWithdrawalFee;

    @Column(name="balanceFee")
    private BigDecimal balanceFee;

    @Column(name="accountType")
    private AccountType accountType;

    @Column(name="isEnabled")
    private boolean isEnabled;

    @Column(name="isAuthorized")
    private boolean isAuthorized;

    @Column(name="postingDate")
    private LocalDate postingDate;
}
