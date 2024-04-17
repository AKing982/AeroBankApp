package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.TransferStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="transfers")
@Getter
@Setter
@EqualsAndHashCode
@ToString
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
    private AccountEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="toAccountID", referencedColumnName = "acctID")
    private AccountEntity toAccount;

    @Column(name="transferAmount")
    private BigDecimal transferAmount;

    @Column(name="description")
    private String description;

    @Column(name="transferDate")
    private LocalDate transferDate;

    @Column(name="transferTime")
    private LocalTime transferTime;

    @Column(name="isPending")
    private boolean isPending;

    @Column(name="transferType")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private TransferStatus status;

    @Override
    public String toString() {
        return "TransferEntity{" +
                "transferID=" + transferID +
                ", toUser=" + toUser +
                ", fromUser=" + fromUser +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", transferAmount=" + transferAmount +
                ", description='" + description + '\'' +
                ", transferDate=" + transferDate +
                ", transferTime=" + transferTime +
                ", isPending=" + isPending +
                ", transferType=" + transferType +
                ", status=" + status +
                '}';
    }
}
