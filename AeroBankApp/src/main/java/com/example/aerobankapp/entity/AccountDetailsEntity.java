package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="accountDetails")
public class AccountDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="historyID", nullable = false)
    @NotNull
    private Long historyID;

    @Column(name="accountID", nullable = false)
    @NotNull
    private int accountID;

    @Column(name="balance", nullable = false)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction=2)
    private BigDecimal balance;

    @Column(name="pending", nullable = false)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction=2)
    private BigDecimal pendingBalance;

    @Column(name="available", nullable = false)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction=2)
    private BigDecimal available;


}