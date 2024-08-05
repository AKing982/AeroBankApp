package com.example.aerobankapp.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class LinkedTransactionInfo
{
    private String transactionId;
    private int sysAcctID;

    public LinkedTransactionInfo(String transactionId, int sysAcctID)
    {
        this.transactionId = transactionId;
        this.sysAcctID = sysAcctID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTransactionInfo that = (LinkedTransactionInfo) o;
        return sysAcctID == that.sysAcctID && Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, sysAcctID);
    }

    @Override
    public String toString() {
        return "LinkedTransactionInfo{" +
                "transactionId='" + transactionId + '\'' +
                ", sysAcctID=" + sysAcctID +
                '}';
    }
}
