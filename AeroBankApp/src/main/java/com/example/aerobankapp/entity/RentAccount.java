package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name="rentAccount")
public class RentAccount
{
    @Id
    private String id;

}
