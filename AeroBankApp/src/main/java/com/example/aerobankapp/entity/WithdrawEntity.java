package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name="withdrawals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawID;

    @ManyToOne
    @JoinColumn(name="userID")
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="fromAcctID")
    @JsonBackReference
    private AccountEntity account;

    @Column(name="description")
    @NotNull
    @Size(min=35, message="Must have atleast 35 characters")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="posted")
    private LocalDate posted;

    @Column(name="scheduledInterval")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduledInterval;

    @Column(name="scheduledTime")
    private LocalTime scheduledTime;

    @Column(name="scheduledDate")
    private LocalDate scheduledDate;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
