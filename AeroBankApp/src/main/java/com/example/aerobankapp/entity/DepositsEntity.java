package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="deposit")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositsEntity extends AbstractTransactionEntityModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depositID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    @JsonBackReference
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
    @JsonBackReference
    private AccountEntity account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transactionCriteriaID")
    private TransactionCriteriaEntity transactionCriteria;

}
