package com.example.aerobankapp.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Builder
public record RegistrationDTO(String firstName,
                              String lastName,
                              String userName,
                              String email,
                              String address,
                              String zipcode,
                              int pinNumber,
                              String password,
                              BigDecimal deposit,
                              boolean isAdmin) {


}
