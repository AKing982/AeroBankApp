package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transfer")
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
    @JoinColumn(name="toUser", referencedColumnName = "userID")
    private UserEntity toUser;

    @ManyToOne
    @JoinColumn(name="fromUser", referencedColumnName = "userID")
    private UserEntity fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fromAcctID")
    private AccountEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="toAcctID")
    private AccountEntity toAccount;

    @Column(name="toAcctName")
    private String toAcctName;

    @Column(name="fromAcctName")
    private String fromAcctName;

    @Column(name="transferAmount")
    private BigDecimal transferAmount;

    @Column(name="isPending")
    private boolean isPending;

    @Column(name="dateTransferred")
    private LocalDate dateTransferred;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private TransferStatus status;
}
