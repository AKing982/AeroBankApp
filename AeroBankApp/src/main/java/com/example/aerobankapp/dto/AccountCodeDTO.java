package com.example.aerobankapp.dto;

public record AccountCodeDTO(String firstInitial, String lastInitial, int userID, String accountType, int year, int sequence) {
}
