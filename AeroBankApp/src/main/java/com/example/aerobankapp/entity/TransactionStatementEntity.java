package com.example.aerobankapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transactionStatement")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statementID;

    @ManyToOne
    @JoinColumn(name="acctID")
    @JsonBackReference
    private AccountEntity accountEntity;

    @Column(name="description")
    private String description;

    @Column(name="debit")
    private BigDecimal debit;

    @Column(name="credit")
    private BigDecimal credit;
    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="transactionDate")
    private String transactionDate;

    @Column(name="isPending")
    private boolean isPending;

}
