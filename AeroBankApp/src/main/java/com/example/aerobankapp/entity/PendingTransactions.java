package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="PendingTransactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class PendingTransactions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="userID")
    private int userID;

    @Column(name="description")
    private String description;

    @Column(name="acctID")
    private String acctID;

    @Column(name="debit")
    private BigDecimal debit;

    @Column(name="credit")
    private BigDecimal credit;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="date")
    private Date date;

    @Column(name="status")
    private String status;

    @Column(name="real_balance")
    private BigDecimal real_Balance;


}
