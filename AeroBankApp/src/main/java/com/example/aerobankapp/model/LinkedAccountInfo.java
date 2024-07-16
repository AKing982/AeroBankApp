package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkedAccountInfo
{
    private int systemAcctID;
    private String externalAcctID;

    public LinkedAccountInfo(int systemAcctID, String externalAcctID) {
        this.systemAcctID = systemAcctID;
        this.externalAcctID = externalAcctID;
    }
}
