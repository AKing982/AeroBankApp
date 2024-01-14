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
@Table(name="rentAccount")
@Deprecated
public class RentAccountEntity
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

    @Column(name="min_balance")
    private BigDecimal min_balance;

    @Column(name="interestRate")
    private BigDecimal interestRate;



}
