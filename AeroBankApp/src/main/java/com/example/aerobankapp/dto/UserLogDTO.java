package com.example.aerobankapp.dto;

import java.util.Date;

public record UserLogDTO(int sessionID,
                         int userID,
                         Date lastLogin)
{

}
