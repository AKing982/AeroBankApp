package com.example.aerobankapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
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

    private char[] password;

    private BigDecimal deposit;

    private boolean isAdmin;

    public RegistrationDTO()
    {

    }

}
