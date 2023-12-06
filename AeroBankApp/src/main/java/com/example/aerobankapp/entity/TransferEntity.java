package com.example.aerobankapp.entity;

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

    @Column(name="toUserID")
    private int toUserID;

    @Column(name="fromUserID")
    private int fromUserID;

    @Column(name="fromAcctID")
    private String fromAcctID;

    @Column(name="toAcctID")
    private String toAcctID;

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
}
