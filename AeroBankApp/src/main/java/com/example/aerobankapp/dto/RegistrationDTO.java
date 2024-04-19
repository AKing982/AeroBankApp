package com.example.aerobankapp.dto;

import com.example.aerobankapp.model.Account;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
