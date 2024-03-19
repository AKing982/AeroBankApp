package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="transactionStatement")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TransactionStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statementID;

    @ManyToOne
    @JoinColumn(name="acctID")
    private AccountEntity accountEntity;

    @Column(name="description")
    private String description;

    @Column(name="debit")
    private String debit;

    @Column(name="credit")
    private String credit;

    @Column(name="balance")
    private String balance;

    @Column(name="transactionDate")
    private String transactionDate;

}