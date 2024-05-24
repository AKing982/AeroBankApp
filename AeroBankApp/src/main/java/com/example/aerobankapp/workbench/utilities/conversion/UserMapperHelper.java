package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;

import java.util.Map;

public class UserMapperHelper
{
    public static UserDTO convertToDTO(UserEntity entity)
    {
        return new UserDTO(entity.getUserID(),
                entity.getUserCredentials().getUsername(), entity.getUserDetails().getEmail(),
                entity.getUserCredentials().getPassword(), entity.getUserSecurity().getPinNumber(),
                entity.getUserDetails().getAccountNumber(),
                entity.getUserDetails().getFirstName(),
                entity.getUserDetails().getLastName(),
                entity.getUserSecurity().isAdmin(), entity.getUserSecurity().isEnabled(), entity.getUserSecurity().getRole());
    }
}
