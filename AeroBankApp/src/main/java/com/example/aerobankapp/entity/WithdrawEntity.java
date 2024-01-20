package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="withdraws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="userID")
    private int userID;

    @Column(name="currency")
    @NotNull
    private String currency;

    @Column(name="fromAccountID")
    @NotNull
    private int fromAccountID;

    @Column(name="fromAccountCode")
    @NotNull
    private String fromAccountCode;

    @Column(name="description")
    @NotNull
    @Size(min=35, message="Must have atleast 35 characters")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="date_posted")
    private LocalDate date_posted;

    @Column(name="isProcessed")
    private boolean isProcessed;

    @Column(name="status")
    private String status;

    @OneToOne
    private TransactionDetailEntity transactionDetailEntity;

}
