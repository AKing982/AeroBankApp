package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="plaidTransactions")
@Data
@Builder
public class PlaidTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="acctID")
//    private AccountEntity account;

    @Column(name="externalAcctID")
    private String externalAcctID;

    @Column(name="externalId")
    @NotNull
    @NotBlank
    private String externalId;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="name")
    private String name;

    @Column(name="pending")
    private boolean pending;

    @Column(name="date")
    private LocalDate date;

    @Column(name="merchantName")
    private String merchantName;

    @Column(name="authorizedDate")
    private LocalDate authorizedDate;

    @Column(name="createdAt")
    private LocalDateTime createdAt;

    public PlaidTransactionEntity(Long id, UserEntity user,String externalAcctID, String externalId, BigDecimal amount, String name, boolean pending, LocalDate date, String merchantName, LocalDate authorizedDate, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.externalAcctID = externalAcctID;
        this.externalId = externalId;
        this.amount = amount;
        this.name = name;
        this.pending = pending;
        this.date = date;
        this.merchantName = merchantName;
        this.authorizedDate = authorizedDate;
        this.createdAt = createdAt;
    }

    public PlaidTransactionEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaidTransactionEntity that = (PlaidTransactionEntity) o;
        return pending == that.pending && Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(externalAcctID, that.externalAcctID) && Objects.equals(externalId, that.externalId) && Objects.equals(amount, that.amount) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(merchantName, that.merchantName) && Objects.equals(authorizedDate, that.authorizedDate) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, externalAcctID, externalId, amount, name, pending, date, merchantName, authorizedDate, createdAt);
    }
}
