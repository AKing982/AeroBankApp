package com.example.aerobankapp.dto;

import java.time.LocalDateTime;
import java.util.Date;


public record UserLogDTO(int sessionID,
                         int userID,
                         LocalDateTime lastLogin)
{

}
