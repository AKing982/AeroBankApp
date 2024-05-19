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
@Table(name="withdraw")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transactionCriteriaID")
    private TransactionCriteriaEntity transactionCriteria;

}
