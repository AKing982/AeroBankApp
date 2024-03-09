package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="transactionDetail")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
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

    @Column(name="newBalance")
    private BigDecimal newBalance;

    @Column(name="createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDetailEntity that = (TransactionDetailEntity) o;
        return Objects.equals(transactionDetailID, that.transactionDetailID)
                && Objects.equals(userEntity, that.userEntity)
                && Objects.equals(accountEntity, that.accountEntity)
                && Objects.equals(relatedAcctEntity, that.relatedAcctEntity)
                && Objects.equals(depositsEntity, that.depositsEntity)
                && Objects.equals(withdrawEntity, that.withdrawEntity)
                && Objects.equals(transferEntity, that.transferEntity)
                && Objects.equals(purchaseEntity, that.purchaseEntity)
                && Objects.equals(balance, that.balance)
                && Objects.equals(newBalance, that.newBalance)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionDetailID,
                userEntity,
                accountEntity,
                relatedAcctEntity,
                depositsEntity,
                withdrawEntity,
                transferEntity,
                purchaseEntity,
                balance,
                newBalance, createdAt);
    }
}
