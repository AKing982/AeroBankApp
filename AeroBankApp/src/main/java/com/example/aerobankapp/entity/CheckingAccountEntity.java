package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="checkingAccount")
@Deprecated
public class CheckingAccountEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="accountCode")
    private String accountCode;

    @Column(name="user_id")
    private int userID;

    @Column(name="a_secid")
    private int aSecID;

    @NotNull
    @Column(name="account_name")
    private String accountName;

    @NotNull
    @Column(name="username")
    private String userName;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="interest_rate")
    private BigDecimal interestRate;

    @Column(name="minimum_balance")
    private BigDecimal minimumBalance;
}
