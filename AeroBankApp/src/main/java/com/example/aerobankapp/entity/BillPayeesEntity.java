package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="BillPayees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPayeesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payeeID;

    @ManyToOne
    @JoinColumn(name="userID")
    private UserEntity user;

    @Column(name="payeeName")
    @NotNull
    @NotBlank
    @NotEmpty
    private String payeeName;

    @Column(name="createdAt")
    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    @Column(name="updatedAt")
    @Temporal(TemporalType.DATE)
    private LocalDate updatedAt;



}
