package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name="registration")
public class Registration {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

    @Column(name="username")
    private String userName;

    @Column(name="email")
    private String email;

    @Column(name="address")
    private String address;

    @Column(name="zipcode")
    private Long zipcode;

    @Column(name="pinNumber")
    private Long pinNumber;

    @Column(name="password")
    private char[] password;

    @Column(name="deposit")
    private BigDecimal deposit;

    @Column(name="isAdmin")
    private boolean isAdmin;

    public Registration(String firstName, String lastName, String email, String address, Long zipcode, Long pinNumber, char[] password, BigDecimal deposit, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
        this.pinNumber = pinNumber;
        this.password = password;
        this.deposit = deposit;
        this.isAdmin = isAdmin;
    }
}
