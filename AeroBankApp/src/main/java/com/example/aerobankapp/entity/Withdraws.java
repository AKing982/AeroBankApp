package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="withdraws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdraws
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="userID")
    private int userID;

    @Column(name="fromAccountID")
    @NotNull
    private String fromAccountID;

    @Column(name="toAccountID")
    @NotNull
    private String toAccountID;

    @Column(name="description")
    @NotNull
    @Size(min=35, message="Must have atleast 35 characters")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="date_posted")
    private LocalDate date_posted;

    @Column(name="isProcessed")
    private boolean isProcessed;



}
