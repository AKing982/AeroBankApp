package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.dto.UserLogDTO;
import com.example.aerobankapp.entity.UserLogEntity;

public class UserLogMapperHelper
{
    public static UserLogDTO convertToDTO(UserLogEntity entity)
    {
        return new UserLogDTO(entity.getId(), entity.getUserID(), entity.getLastLogin());
    }
}
