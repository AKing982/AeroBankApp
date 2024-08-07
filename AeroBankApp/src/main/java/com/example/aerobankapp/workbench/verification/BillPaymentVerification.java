package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentVerification implements PaymentVerifier<BillPayment>
{
    private final BillPaymentDataManager billPaymentDataManager;

    @Autowired
    public BillPaymentVerification(BillPaymentDataManager billPaymentDataManager) {
        this.billPaymentDataManager = billPaymentDataManager;
    }

    @Override
    public Boolean verify(BillPayment payment) {
        return false;
    }
}
