package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="accountSecurity")
public class AccountSecurityEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long accountSecurityID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="acctID", unique = true, nullable = false)
    private AccountEntity account;

    @Column(name="isEnabled")
    private boolean isEnabled;

    @Column(name="depositLimit")
    private int depositLimit;

    @Column(name="withdrawLimit")
    private int withdrawLimit;

    @Column(name="transferLimit")
    private int transferLimit;

    @Column(name="transactionVelocityLimit")
    private int transactionVelocityLimit;

    @Column(name="isWithdrawEnabled")
    private boolean isWithdrawEnabled;

    @Column(name="isDepositEnabled")
    private boolean isDepositEnabled;

    @Column(name="isTransferEnabled")
    private boolean isTransferEnabled;

    @Column(name="isPurchaseEnabled")
    private boolean isPurchaseEnabled;

    @Column(name="isAccountLocked")
    private boolean isAccountLocked;

    @Column(name="autoPayEnabled")
    private boolean autoPayEnabled;

    @Column(name="minimumBalance")
    @DecimalMin("0.00")
    @NotNull
    private BigDecimal minimumBalance;

    @Column(name="feesEnabled")
    private boolean feesEnabled;

    @Column(name="interestEnabled")
    private boolean interestEnabled;

    @Override
    public String toString() {
        return "AccountSecurityEntity{" +
                "accountSecurityID=" + accountSecurityID +
                ", account=" + account +
                ", isEnabled=" + isEnabled +
                ", depositLimit=" + depositLimit +
                ", withdrawLimit=" + withdrawLimit +
                ", transferLimit=" + transferLimit +
                ", transactionVelocityLimit=" + transactionVelocityLimit +
                ", isWithdrawEnabled=" + isWithdrawEnabled +
                ", isDepositEnabled=" + isDepositEnabled +
                ", isTransferEnabled=" + isTransferEnabled +
                ", isPurchaseEnabled=" + isPurchaseEnabled +
                ", isAccountLocked=" + isAccountLocked +
                ", autoPayEnabled=" + autoPayEnabled +
                ", minimumBalance=" + minimumBalance +
                ", feesEnabled=" + feesEnabled +
                ", interestEnabled=" + interestEnabled +
                '}';
    }
}
