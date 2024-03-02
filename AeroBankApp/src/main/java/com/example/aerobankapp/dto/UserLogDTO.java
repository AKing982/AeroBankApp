package com.example.aerobankapp.dto;

import java.time.LocalDateTime;
import java.util.Date;


public record UserLogDTO(int sessionID,
                         int userID,
                         String username,
                         LocalDateTime lastLogin,
                         LocalDateTime lastLogout,
                         int sessionDuration,
                         boolean loginSuccess,
                         String ipAddress,

                         String sessionToken)
{

}
