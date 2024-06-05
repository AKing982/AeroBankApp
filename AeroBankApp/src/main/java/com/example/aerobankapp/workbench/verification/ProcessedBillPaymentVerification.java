package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.model.ProcessedBillPayment;
import org.springframework.stereotype.Component;

@Component
public class ProcessedBillPaymentVerification implements PaymentVerifier<ProcessedBillPayment>
{

    @Override
    public boolean verify(ProcessedBillPayment payment) {
        return false;
    }

    public boolean checkPaymentAmount(ProcessedBillPayment payment) {
        return false;
    }

    public boolean checkPaymentDate(ProcessedBillPayment payment) {
        return false;
    }

    public boolean checkPayeeDetails(ProcessedBillPayment payment) {
        return false;
    }

    public boolean validatePaymentDetails(ProcessedBillPayment payment) {
        return false;
    }

}
