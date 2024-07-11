package com.example.aerobankapp.converter;

import com.example.aerobankapp.model.PlaidAccountBalances;
import com.plaid.client.model.AccountBase;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@NoArgsConstructor
@Component
public class AccountBaseToPlaidAccountBalancesConverter implements ModelConverter<PlaidAccountBalances, AccountBase>
{

    @Override
    public PlaidAccountBalances convert(AccountBase model) {
        PlaidAccountBalances plaidAccountBalances = new PlaidAccountBalances();
        plaidAccountBalances.setAvailableBalance(BigDecimal.valueOf(model.getBalances().getAvailable()));
        plaidAccountBalances.setCurrentBalance(BigDecimal.valueOf(model.getBalances().getCurrent()));
        plaidAccountBalances.setAccountId(model.getAccountId());
        return null;
    }
}
