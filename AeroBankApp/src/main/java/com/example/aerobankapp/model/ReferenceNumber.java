package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class ReferenceNumber
{
    private String referenceValue;
    private LocalDate issuedOn;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String accountReferenceCode;

    public ReferenceNumber(String numParam, LocalDate issued, TransactionType type, TransactionStatus status, String acctRefCode){
        this.referenceValue = numParam;
        this.issuedOn = issued;
        this.transactionType = type;
        this.transactionStatus = status;
        this.accountReferenceCode = acctRefCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceNumber that = (ReferenceNumber) o;
        return Objects.equals(referenceValue, that.referenceValue) && Objects.equals(issuedOn, that.issuedOn) && transactionType == that.transactionType && transactionStatus == that.transactionStatus && Objects.equals(accountReferenceCode, that.accountReferenceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceValue, issuedOn, transactionType, transactionStatus, accountReferenceCode);
    }

    @Override
    public String toString() {
        return "ReferenceNumber{" +
                "referenceValue='" + referenceValue + '\'' +
                ", issuedOn=" + issuedOn +
                ", transactionType=" + transactionType +
                ", transactionStatus=" + transactionStatus +
                ", accountReferenceCode='" + accountReferenceCode + '\'' +
                '}';
    }
}