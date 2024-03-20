package com.example.aerobankapp.workbench.utilities;

import java.math.BigDecimal;

@FunctionalInterface
public interface CalculationStrategy
{
    BigDecimal calculate(BigDecimal amount, BigDecimal balance);
}
