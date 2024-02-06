package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="deposits")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depositID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
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
    private LocalDateTime scheduledTime;

    @Column(name="scheduledDate")
    private LocalDate scheduledDate;

    @Column(name="posted")
    private LocalDate posted;

}
