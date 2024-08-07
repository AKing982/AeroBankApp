package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="transfer")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferID;

    @ManyToOne
    @JoinColumn(name="toUserID", referencedColumnName = "userID")
    private UserEntity toUser;

    @ManyToOne
    @JoinColumn(name="fromUserID", referencedColumnName = "userID")
    private UserEntity fromUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="fromAccountID", referencedColumnName = "acctID")
    @JsonBackReference
    private AccountEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="toAccountID", referencedColumnName = "acctID")
    @JsonBackReference
    private AccountEntity toAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="criteriaID")
    private TransactionCriteriaEntity criteria;

    @Column(name="transferType")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Override
    public String toString() {
        return "TransferEntity{" +
                "transferID=" + transferID +
                ", toUser=" + toUser +
                ", fromUser=" + fromUser +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", criteria=" + criteria +
                ", transferType=" + transferType +
                '}';
    }
}
