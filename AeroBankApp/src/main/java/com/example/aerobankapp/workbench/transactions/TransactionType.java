package com.example.aerobankapp.workbench.transactions;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),

    PURCHASE("Purchase"),

    TRANSFER("Transfer");

    private String code;

    TransactionType(String code){
        this.code = code;
    }
}
