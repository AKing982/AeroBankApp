package com.example.aerobankapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="customer")
public class Customer
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="firstName")
    private String firstName;


}
