package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name="nextPaymentDate", nullable = false)
    private LocalDate nextPayment;

    @Column(name="lastPaymentDate")
    private LocalDate lastPayment;

    @Temporal(TemporalType.DATE)
    @Column(name="dateUpdated")
    private LocalDate dateUpdated;

    @Column(name="isProcessed")
    private boolean isProcessed;

}
