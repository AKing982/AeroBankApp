package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="transactionDetail")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class TransactionDetailEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionDetailID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="acctID")
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name="relatedAcctID")
    private AccountEntity relatedAcctEntity;

    @ManyToOne
    @JoinColumn(name="depositID")
    private DepositsEntity depositsEntity;

    @ManyToOne
    @JoinColumn(name="withdrawID")
    private WithdrawEntity withdrawEntity;

    @ManyToOne
    @JoinColumn(name="transferID")
    private TransferEntity transferEntity;

    @ManyToOne
    @JoinColumn(name="purchaseID")
    private PurchaseEntity purchaseEntity;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="createdAt")
    private LocalDateTime createdAt;

    @Column(name="debit", nullable = false)
    @NotNull
    private BigDecimal debit;

    @Column(name="credit", nullable = false)
    @NotNull
    private BigDecimal credit;

    @Column(name="type", nullable = false)
    @NotEmpty
    @NotBlank
    private String type;

    @Column(name="status", nullable = false)
    @NotEmpty
    @NotBlank
    private String status;

    @Column(name="method")
    @NotEmpty
    @NotBlank
    private String method;

    @Column(name="payee", nullable = false)
    @NotBlank
    @NotEmpty
    @NotNull
    private String payee;

    @Column(name="description", nullable = false)
    @NotNull
    @NotBlank
    @NotEmpty
    private String description;

    @Column(name="referenceNumber", nullable = false)
    @NotNull
    @NotBlank
    @NotEmpty
    private String referenceNumber;

    @Column(name="confirmationNumber", nullable = false)
    @NotNull
    private Integer confirmationNumber;

    @Column(name="fee",  nullable = false)
    @NotNull
    private BigDecimal fee;



}
