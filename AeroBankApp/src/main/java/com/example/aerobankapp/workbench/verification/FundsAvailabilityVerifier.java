package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.model.Account;

public class FundsAvailabilityVerifier implements PaymentVerifier<Account>
{

    @Override
    public Boolean verify(Account payment) {
        return false;
    }
}

