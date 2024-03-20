package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="fromAcctID")
    private AccountEntity account;

    @Column(name="description")
    @NotNull
    @Size(min=35, message="Must have atleast 35 characters")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="posted")
    private LocalDate posted;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
