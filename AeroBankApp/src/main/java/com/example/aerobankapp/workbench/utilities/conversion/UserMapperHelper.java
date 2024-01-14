package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;

import java.util.Map;

public class UserMapperHelper
{
    public static UserDTO convertToDTO(UserEntity entity)
    {
        return new UserDTO(entity.getUserID(),
                entity.getUsername(), entity.getEmail(),
                entity.getPassword(), entity.getPinNumber(),
                entity.isAdmin(), entity.isEnabled(), entity.getRole());
    }
}
