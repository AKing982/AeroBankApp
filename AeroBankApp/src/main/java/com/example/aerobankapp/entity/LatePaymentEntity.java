package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Table(name="latePayment")
@Entity
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

    public LatePaymentEntity(Long id, BillPaymentEntity billPayment, LocalDate originalDueDate, BigDecimal lateFee, Integer daysLate, Date createdAt) {
        this.id = id;
        this.billPayment = billPayment;
        this.originalDueDate = originalDueDate;
        this.lateFee = lateFee;
        this.daysLate = daysLate;
        this.createdAt = createdAt;
    }

    public LatePaymentEntity()
    {

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillPaymentEntity getBillPayment() {
        return billPayment;
    }

    public void setBillPayment(BillPaymentEntity billPayment) {
        this.billPayment = billPayment;
    }

    public LocalDate getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(LocalDate originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public Integer getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(Integer daysLate) {
        this.daysLate = daysLate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
