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

    @Column(name="amount")
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;

    @Column(name="description")
    @NotEmpty
    @NotBlank
    private String description;

    @Column(name="referenceNumber")
    @NotEmpty
    @NotBlank
    private String referenceNumber;

    @Column(name="confirmationNumber")
    @NotNull
    @Size(max=7)
    private Integer confirmationNumber;

    @Column(name="transactionStatus")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name="posted")
    @Temporal(TemporalType.DATE)
    private LocalDate posted;

    @Column(name="notificationEnabled")
    private boolean notificationEnabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transactionScheduleID")
    private TransactionScheduleCriteriaEntity transactionScheduleCriteria;

}
