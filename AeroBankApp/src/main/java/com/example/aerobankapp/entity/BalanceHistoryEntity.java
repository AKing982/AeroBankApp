package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@Table(name="balanceHistory")
public class BalanceHistoryEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyID;

    @ManyToOne
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @Column(name="postBalance")
    private BigDecimal postBalance;

    @Column(name="adjusted")
    private BigDecimal adjusted;

    @Column(name="preBalance")
    private BigDecimal preBalance;

    @Column(name="transactionType")
    private String transactionType;

    @Column(name="createdBy")
    private String createdBy;

    @Column(name="createdAt")
    private LocalDateTime createdAt;

    @Column(name="posted")
    private LocalDate posted;

    @Override
    public String toString() {
        return "BalanceHistoryEntity{" +
                "historyID=" + historyID +
                ", account=" + account +
                ", postBalance=" + postBalance +
                ", adjusted=" + adjusted +
                ", previousBalance=" + preBalance +
                ", transactionType='" + transactionType + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", posted=" + posted +
                '}';
    }
}
