package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="transactions")
public class TransactionEntity
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long transactionID;

    @Column(name="transactionType")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name="referenceID")
    private Long referenceID;

    @Column(name="userID")
    private int userID;

    @Column(name="acctID")
    private String acctID;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="debit")
    private BigDecimal debit;

    @Column(name="credit")
    private BigDecimal credit;

    @Column(name="description")
    private String description;

    @Column(name="posted")
    private LocalDateTime posted;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
