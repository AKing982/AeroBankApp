package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.model.PlaidAccountBalances;
import com.plaid.client.model.AccountBase;

import java.math.BigDecimal;

@Deprecated
public class AccountBaseToAccountBalancesUtil
{
    public static PlaidAccountBalances convertToAccountBalances(AccountBase accountBase)
    {
        PlaidAccountBalances accountBalances = new PlaidAccountBalances();
        accountBalances.setAvailableBalance(getBigDecimalBalance(getAvailable(accountBase)));
        accountBalances.setCurrentBalance(getBigDecimalBalance(getCurrentBalance(accountBase)));
        accountBalances.setAccountId(accountBase.getAccountId());
        return accountBalances;
    }

    private static double getCurrentBalance(AccountBase accountBase)
    {
        return accountBase.getBalances().getCurrent();
    }

    private static double getAvailable(AccountBase accountBase)
    {
        return accountBase.getBalances().getAvailable();
    }

    private static BigDecimal getBigDecimalBalance(double param)
    {
        return BigDecimal.valueOf(param);
    }
}
