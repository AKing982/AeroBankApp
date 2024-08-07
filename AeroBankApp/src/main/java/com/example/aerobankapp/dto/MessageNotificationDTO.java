package com.example.aerobankapp.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageNotificationDTO(String title,
                                     String message,
                                     String receiver,
                                     String sender,
                                     int userID,
                                     LocalDateTime sent) {
}
