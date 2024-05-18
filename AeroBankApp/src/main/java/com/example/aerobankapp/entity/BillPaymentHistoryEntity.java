package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="billPaymentHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentHistoryEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentHistoryID;

    @Column(name="nextPaymentDate")
    private LocalDate nextPayment;

    @Column(name="lastPaymentDate")
    private LocalDate lastPayment;

    @Column(name="dateUpdated")
    private LocalDate dateUpdated;

}
