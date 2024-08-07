package com.example.aerobankapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="administrator")
@Deprecated
public class Administrator
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
}
