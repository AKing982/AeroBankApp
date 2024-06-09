package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Table(name="latePayment")
@Entity
@Data
public class LatePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="billPaymentID", nullable=false)
    private BillPaymentEntity billPayment;

    @Column(name="originalDueDate")
    private LocalDate originalDueDate;

    @Column(name="lateFee")
    private BigDecimal lateFee;

    @Column(name="daysLate")
    private Integer daysLate;

    @Column(name="createdAt", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
