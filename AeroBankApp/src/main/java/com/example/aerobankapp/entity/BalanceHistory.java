package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Table(name="balanceHistory")
public class BalanceHistory implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="acctID")
    private String acctID;

    @Column(name="user")
    private String user;

    @Column(name="transactionID")
    private int transactionID;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="adjusted")
    private BigDecimal adjusted;

    @Column(name="lastBalance")
    private BigDecimal lastBalance;

    @Column(name="posted")
    private LocalDate posted;
}
