package com.example.aerobankapp.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="userID")
    private int userID;

    @Column(name="fromAccountID")
    private String fromAccountID;

    @Column(name="toAccountID")
    private String toAccountID;

    @Column(name="description")
    private String description;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="date_posted")
    private LocalDate date_posted;

    @Column(name="isProcessed")
    private boolean isProcessed;



}
