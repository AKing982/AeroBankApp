package com.example.aerobankapp.fees;

import com.example.aerobankapp.workbench.processor.LatePaymentFees;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FeeManagerImpl implements FeeManager
{

    public FeeManagerImpl(){

    }

    @Override
    public BigDecimal calculateFeeByDaysLate(int daysLate) {
        switch (daysLate) {
            case 1:
                return LatePaymentFees.ONE_DAY;
            case 2:
                return LatePaymentFees.TWO_DAY;
            case 3:
                return LatePaymentFees.THREE_DAY;
            case 7:
                return LatePaymentFees.SEVEN_DAY;
            case 14:
                return LatePaymentFees.TWO_WEEKS;
            default:
                throw new IllegalArgumentException("Invalid daysLate: " + daysLate);
        }
    }
}
