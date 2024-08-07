package com.example.aerobankapp.workbench.utilities;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatterUtil
{
    public static String formatAmount(BigDecimal amount, Locale locale, int decimalPlaces) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        currencyFormatter.setMinimumFractionDigits(decimalPlaces);
        currencyFormatter.setMaximumFractionDigits(decimalPlaces);
        return currencyFormatter.format(amount);
    }
}
