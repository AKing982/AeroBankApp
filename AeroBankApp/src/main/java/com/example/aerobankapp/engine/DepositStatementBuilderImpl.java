package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.TransactionEntity;
import com.example.aerobankapp.exceptions.InvalidDepositException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.services.PendingTransactionsService;
import com.example.aerobankapp.services.TransactionService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.CurrencyFormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class DepositStatementBuilderImpl extends AbstractTransactionStatementBuilder<Deposit>
{
    @Autowired
    public DepositStatementBuilderImpl(TransactionService transactionService,
                                       PendingTransactionsService pendingTransactionsService){
        super(transactionService, pendingTransactionsService);
    }

    @Override
    protected BigDecimal getCurrentBalance() {
        return null;
    }


    @Override
    protected String getFormattedDate(final LocalDateTime time)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        if(time == null){
            throw new IllegalArgumentException("Invalid Date Criteria has been found.");
        }
        return time.format(dateTimeFormatter);
    }

    @Override
    protected String getFormattedAmount(final BigDecimal amount) {
        Locale usLocale = Locale.US;
        final int FIXED_DECIMAL = 2;
        if(amount == null){
            throw new InvalidDepositException("Invalid Deposit Amount found.");
        }
        return CurrencyFormatterUtil.formatAmount(amount, usLocale, FIXED_DECIMAL);
    }


    @Override
    protected List<Deposit> retrieveTransactionsByType(List<TransactionEntity> transactionEntities) {
        if(transactionEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to retrieve transactions as list is empty.");
        }
        return null;
    }

    @Override
    public TransactionStatement buildTransactionStatement(List<Deposit> transactionList) {
        return null;
    }

    @Override
    public String buildTransactionDescriptionByTransactionType(TransactionType transactionType) {
        return null;
    }
}
