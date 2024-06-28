package com.example.aerobankapp.converter;

import com.example.aerobankapp.model.PlaidAccount;
import com.plaid.client.model.AccountBase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountBaseToPlaidAccountConverter implements ModelConverter<PlaidAccount, AccountBase>
{


    @Override
    public PlaidAccount convert(AccountBase model) {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setAccountId(model.getAccountId());
        plaidAccount.setMask(model.getMask());
        plaidAccount.setLimit(null);
        plaidAccount.setName(model.getName());
        plaidAccount.setSubtype(String.valueOf(model.getSubtype()));
        plaidAccount.setType(String.valueOf(model.getType()));
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(model.getBalances().getAvailable()));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(model.getBalances().getCurrent()));
        plaidAccount.setOfficialName(model.getOfficialName());
        plaidAccount.setVerificationStatus(String.valueOf(model.getVerificationStatus()));
        return plaidAccount;
    }
}
