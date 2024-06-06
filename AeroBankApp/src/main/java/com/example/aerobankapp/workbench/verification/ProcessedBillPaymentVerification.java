package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.BillPaymentHistory;
import com.example.aerobankapp.model.ProcessedBillPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessedBillPaymentVerification implements PaymentVerifier<ProcessedBillPayment>
{
    private Logger LOGGER = LoggerFactory.getLogger(ProcessedBillPaymentVerification.class);

    @Override
    public boolean verify(ProcessedBillPayment payment) {
        if(payment == null){
            throw new InvalidProcessedBillPaymentException("Processed bill payment cannot be null.");
        }

//        if(!paymentCriteriaVerified(processedBillPayment)){
//            return false;
//        }
        Long paymentID = getProcessedBillPaymentID(billPaymentHistory);
        try{
            validatePaymentWithDB(paymentID);
        } catch(Exception e) {
            LOGGER.error("Error while processing the payment", e);
        }
        return isPaymentProcessed(paymentID);
        return false;
    }

    public boolean verifyPaymentHistory(BillPaymentHistory billPaymentHistory){
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
