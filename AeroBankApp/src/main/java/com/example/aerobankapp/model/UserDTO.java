package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountNumber;
import lombok.*;


@Builder
public record UserDTO(int id,
                      String userName,
                      String email,
                      AccountNumber accountNumber,
                      String password,
                      String pinNumber) {
}
