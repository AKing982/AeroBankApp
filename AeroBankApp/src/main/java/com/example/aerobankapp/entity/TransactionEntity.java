package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="historyID")
    private BalanceHistoryEntity balanceHistory;

    @Column(name="amount")
    @NotNull
    private BigDecimal amount;

    @Column(name="description")
    @NotNull
    @Size(min=13, max=225, message="Character length needs to be between 13 and 225 characters")
    private String description;

    @Column(name="posted")
    private LocalDateTime posted;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;
}