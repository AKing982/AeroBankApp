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
@Table(name="deposits")
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

    @Column(name="amount", nullable = false)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction = 2)
    private BigDecimal amount;

    @Column(name="description")
    @NotNull
    private String description;

    @Column(name="scheduledInterval")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleInterval;

    @Column(name="scheduledTime")
    private LocalTime scheduledTime;

    @Column(name="scheduledDate")
    private LocalDate scheduledDate;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name="posted")
    private LocalDate posted;

}
