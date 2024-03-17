package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;


public interface TransactionStatementBuilder
{
    String format(BigDecimal amount);
    String getDescriptionCode(TransactionType transactionType);
    TransactionStatement getTransactionStatement(String description, BigDecimal balance, BigDecimal debit, BigDecimal credit);
}
