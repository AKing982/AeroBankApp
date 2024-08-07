package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.Role;
import lombok.Builder;

@Builder
public record UserDTO(int userID,

                      String firstName,
                      String lastName,
                      String userName,
                      String email,
                      String password,
                      String pinNumber,
                      String accountNumber,
                      boolean isAdmin,
                      boolean isEnabled,
                      Role role)
{

}
