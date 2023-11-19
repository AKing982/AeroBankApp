package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.account.AccountID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;



@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CheckingAccount")
public class CheckingAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
