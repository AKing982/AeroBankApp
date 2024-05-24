package com.example.aerobankapp.entity;


import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transactionCriteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCriteriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionCriteriaID;

    @Column(name="amount", nullable = false)
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;

    @Column(name="description", nullable = false)
    @NotEmpty
    @NotBlank
    private String description;

    @Column(name="referenceNumber", nullable = false)
    @NotEmpty
    @NotBlank
    private String referenceNumber;

    @Column(name="confirmationNumber", nullable = false)
    @NotNull
    @Size(max=7)
    private Integer confirmationNumber;

    @Column(name="transactionStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name="posted")
    private LocalDate posted;

    @Column(name="notificationEnabled")
    private boolean notificationEnabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transactionScheduleID")
    private TransactionScheduleCriteriaEntity transactionScheduleCriteria;

}
