package com.example.aerobankapp.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RegistrationDTO
{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String address;
    private int zipcode;
    private int pinNumber;
    private String password;
    private BigDecimal deposit;
    private boolean isAdmin;

}
