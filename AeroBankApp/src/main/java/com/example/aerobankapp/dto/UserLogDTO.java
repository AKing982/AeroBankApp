package com.example.aerobankapp.dto;

import java.time.LocalDateTime;
import java.util.Date;


public record UserLogDTO(Long id,
                         int userID,
                         boolean isActive,
                         String lastLogin,
                         String lastLogout,
                         int sessionDuration,
                         boolean loginSuccess,
                         int loginAttempts)
{

}
