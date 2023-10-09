package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="checkingSecurity")
public class CheckingSecurity
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


}
