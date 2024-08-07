package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.Role;
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
                              Role role,
                              String password) {
}
