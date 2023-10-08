package com.example.aerobankapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="registration")
public class Registration {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
}
