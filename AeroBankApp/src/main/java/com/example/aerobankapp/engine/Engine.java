package com.example.aerobankapp.engine;

import java.math.BigDecimal;
import java.util.Currency;

public abstract class Engine
{
    protected abstract BigDecimal calculateTransactionFee();
    protected abstract BigDecimal convertCurrency(BigDecimal amount, Currency fromCurrency, Currency toCurrency);
    protected abstract BigDecimal calculateInterest(BigDecimal amount, BigDecimal annualInterestRate);
    protected abstract BigDecimal calculateTax(BigDecimal amount);

}
