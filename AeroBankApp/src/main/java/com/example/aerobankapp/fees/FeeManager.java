package com.example.aerobankapp.fees;

import java.math.BigDecimal;

public interface FeeManager
{
    BigDecimal calculateFeeByDaysLate(int daysLate);
}
