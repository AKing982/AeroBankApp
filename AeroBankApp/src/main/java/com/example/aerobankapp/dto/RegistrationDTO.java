package com.example.aerobankapp.dto;

import lombok.Builder;

@Builder
public record RegistrationDTO(String firstName,
                              String lastName,
                              String userName,
                              String email,
                              String street,
                              String city,
                              String zip,
                              String PIN,
                              String password,
                              String confirmPassword) {
}
