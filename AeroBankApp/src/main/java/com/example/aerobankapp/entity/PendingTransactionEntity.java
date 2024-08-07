package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="pendingTransactions")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PendingTransactionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pendingID;

    @ManyToOne
    @JoinColumn(name="userID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @Column(name="description")
    private String description;

    @Column(name="pendingAmount")
    private BigDecimal pendingAmount;

    @Column(name="initiatedAt")
    private LocalDateTime initiatedAt;

    @Column(name="status")
    private Status status;
}
