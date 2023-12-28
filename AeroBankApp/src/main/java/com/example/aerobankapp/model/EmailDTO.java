package com.example.aerobankapp.model;

import lombok.*;


@Builder
public record EmailDTO(int userID,
                       String sender,
                       String recipient,
                       String description,
                       String outgoingServer,
                       String port,
                       boolean isTLS,
                       boolean isSSL) {

}
