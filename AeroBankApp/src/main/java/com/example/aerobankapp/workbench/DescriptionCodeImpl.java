package com.example.aerobankapp.workbench;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class DescriptionCodeImpl implements DescriptionCode
{
    private TransactionCode transactionCode;
    private TransactionType transactionType;
    private String transactionDescription;
    private String accountCode;
    private LocalDate transactionDate;

    public DescriptionCodeImpl(TransactionType transactionType,
                               String description,
                               String accountCode,
                               LocalDate transactionDate)
    {
        this.transactionType = transactionType;
        this.transactionDescription = description;
        this.accountCode = accountCode;
        this.transactionDate = transactionDate;
        Objects.requireNonNull(transactionType, "TransactionType cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(accountCode, "Account Code cannot be null");
        Objects.requireNonNull(transactionDate, "TransactionDate cannot be null");
    }

    @Override
    public String build() {
        TransactionType transactionType1 = getTransactionType();
        switch(transactionType1)
        {
            case DEPOSIT -> {
                return descriptionBuilder(TransactionCode.DEPOSIT,
                                         getTransactionDescription(),
                                         getAccountCode(),
                                         getTransactionDate());
            }
            case PURCHASE -> {
                return descriptionBuilder(TransactionCode.PURCHASE,
                        getTransactionDescription(),
                        getAccountCode(),
                        getTransactionDate());
            }
            case TRANSFER -> {
                return descriptionBuilder(TransactionCode.TRANSFER,
                        getTransactionDescription(),
                        getAccountCode(),
                        getTransactionDate());
            }
            case WITHDRAW -> {
                return descriptionBuilder(TransactionCode.WITHDRAW,
                        getTransactionDescription(),
                        getAccountCode(),
                        getTransactionDate());
            }
            default -> {
                throw new IllegalArgumentException("Invalid Transaction Type.");
            }
        }
    }

    private String descriptionBuilder(TransactionCode transactionCode, String description, String acctCode, LocalDate date){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(transactionCode);
        stringBuilder.append(" - ");
        stringBuilder.append(description);
        stringBuilder.append(" - ");
        stringBuilder.append(acctCode);
        stringBuilder.append(" - ");
        stringBuilder.append(date);
        return stringBuilder.toString();
    }
}
