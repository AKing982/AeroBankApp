package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AbstractAccountBase;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class InvestmentAccount extends AbstractAccountBase
{
    private BigDecimal dividend_amt;
    private InvestmentStrategy investmentStrategy;
    private LiquidityRules liquidityRules;
    private Returns returns;

    @Override
    protected void deposit(BigDecimal amount) {

    }

    @Override
    protected void withdraw(BigDecimal amount) {

    }

    @Override
    protected BigDecimal getBalance() {
        return null;
    }
}
