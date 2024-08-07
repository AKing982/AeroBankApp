package com.example.aerobankapp.workbench.verification;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.BillPaymentHistory;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * ProcessedBillPaymentVerification class is responsible for verifying processed bill payments.
 */
@Component
public class ProcessedBillPaymentVerification implements PaymentVerifier<ProcessedBillPayment>
{
    private Logger LOGGER = LoggerFactory.getLogger(ProcessedBillPaymentVerification.class);
    private BillPaymentDataManager billPaymentDataManager;

    @Autowired
    public ProcessedBillPaymentVerification(BillPaymentDataManager billPaymentDataManager) {
        this.billPaymentDataManager = billPaymentDataManager;
    }

    @Override
    public Boolean verify(ProcessedBillPayment payment) {
        if(payment == null){
            throw new InvalidProcessedBillPaymentException("Processed bill payment cannot be null.");
        }

        return null;
    }

    /**
     * Verifies if the payment history has been saved.
     *
     * @param billPaymentHistory The payment history to be verified.
     * @return True if the payment history has been saved, false otherwise.
     */
    public Boolean verifyPaymentHistorySaved(BillPaymentHistory billPaymentHistory){
        if(billPaymentHistory == null){
            throw new InvalidProcessedBillPaymentException("Bill payment history cannot be null.");
        }
        LocalDate lastProcessed = billPaymentHistory.getLastPaymentDate();
        LocalDate nextProcessed = billPaymentHistory.getNextPaymentDate();
        if(lastProcessed == null || nextProcessed == null){
            return false;
        }

        Optional<BillPaymentHistoryEntity> optionalBillPaymentHistoryEntity = billPaymentDataManager.getBillPaymentHistoryByPaymentCriteria(lastProcessed, nextProcessed, true, billPaymentHistory.getPaymentId());
        return optionalBillPaymentHistoryEntity.isPresent();
    }

    public boolean validatePaymentDetails(ProcessedBillPayment payment) {
        return payment.isComplete() && payment.getLastProcessedDate() != null;
    }

}
