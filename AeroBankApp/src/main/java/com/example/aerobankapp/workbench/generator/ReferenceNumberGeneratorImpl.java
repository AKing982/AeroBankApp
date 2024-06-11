package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.ReferenceNumber;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;


public class ReferenceNumberGeneratorImpl implements ReferenceNumberGenerator
{
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String acctRefCode;

    public ReferenceNumberGeneratorImpl(){

    }

    public ReferenceNumberGeneratorImpl(TransactionType type, TransactionStatus transactionStatus, String acctRefCode){
        // Basic constructor
        if(type == null || transactionStatus == null || (acctRefCode == null || acctRefCode.isEmpty())){
            throw new IllegalArgumentException("Caught invalid Parameters to generate reference number.");
        }
        this.transactionType = type;
        this.transactionStatus = transactionStatus;
        this.acctRefCode = acctRefCode;
    }

    @Override
    public ReferenceNumber generateReferenceNumber() {
        // Generate the unique reference value
        String refVal = generateReferenceValue();

        // Generate the issued date
        LocalDate issuedOn = getIssuedDate();

        //return createReferenceNumber(refVal, issuedOn, transactionType, transactionStatus, acctRefCode);
        return createReferenceNumber(refVal, issuedOn, transactionType, transactionStatus, acctRefCode);
    }

    private String generateReferenceValue(){
        return UUID.randomUUID().toString();
    }

    private LocalDate getIssuedDate(){
        return LocalDate.now();
    }

    private ReferenceNumber createReferenceNumber(String refVal, LocalDate issued, TransactionType type, TransactionStatus status, String acctRefCode){
        return new ReferenceNumber(refVal, issued, type, status, acctRefCode);
    }

}
