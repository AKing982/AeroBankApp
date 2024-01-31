package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="deposits")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depositID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountID")
    private AccountEntity account;

    @Column(name="accountCode", nullable = false)
    @NotNull
    @Size(max=2, message="Maximum of 2 characters required")
    private String accountCode;

    @Column(name="amount", nullable = false)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction = 2)
    private BigDecimal amount;

    @Column(name="isDebit", nullable = false, columnDefinition = "DEFAULT FALSE")
    private boolean isDebit;

    @Column(name="isBankTransfer", nullable = false, columnDefinition = "DEFAULT FALSE")
    private boolean isBankTransfer;

    @Column(name="date_posted")
    private LocalDate date_posted;

}
