package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="billPayment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @Column(name="payeeName", nullable = false)
    @NotBlank
    @NotEmpty
    @NotNull
    private String payeeName;

    @Column(name="paymentAmount", nullable = false)
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal paymentAmount;

    @Column(name="paymentType", nullable = false)
    @NotBlank
    @NotEmpty
    @NotNull
    private String paymentType;

    @Column(name="postedDate")
    @NotBlank
    @NotEmpty
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate postedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paymentScheduleID")
    private BillPaymentScheduleEntity paymentSchedule;

}
