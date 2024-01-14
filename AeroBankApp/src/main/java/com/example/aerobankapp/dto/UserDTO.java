package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.Role;

public record UserDTO(int userID,
                      String userName,
                      String email,
                      String password,
                      String pinNumber,
                      boolean isAdmin,
                      boolean isEnabled,
                      Role role)
{

}
