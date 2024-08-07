package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Model representing linked account information.
 */
@Data
@NoArgsConstructor
public class LinkedAccountInfo
{
    private Integer systemAcctID;
    private String externalAcctID;

    public LinkedAccountInfo(Integer systemAcctID, String externalAcctID) {
        this.systemAcctID = systemAcctID;
        this.externalAcctID = externalAcctID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedAccountInfo that = (LinkedAccountInfo) o;
        return systemAcctID == that.systemAcctID && Objects.equals(externalAcctID, that.externalAcctID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemAcctID, externalAcctID);
    }

    @Override
    public String toString() {
        return "LinkedAccountInfo{" +
                "systemAcctID=" + systemAcctID +
                ", externalAcctID='" + externalAcctID + '\'' +
                '}';
    }
}
