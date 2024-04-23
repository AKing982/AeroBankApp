package com.example.aerobankapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegistrationDTO(String firstName,
                              String lastName,
                              String username,
                              String email,
                              List<AccountInfoDTO> accounts,
                              List<SecurityQuestionsDTO> securityQuestions,
                              String PIN,
                              boolean isAdmin,
                              String password) {
}
