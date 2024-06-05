package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Service
public class LatePaymentProcessor implements PaymentProcessor<LateBillPayment>
{
    private TreeMap<LocalDate, List<LateBillPayment>> latePayments = new TreeMap<>();

    public LatePaymentProcessor(){

    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(LateBillPayment payment) {
        return null;
    }

    public BigDecimal getTotalLatePayments(){
        return null;
    }

    public void processLatePayment(LateBillPayment lateBillPayment) {
        BigDecimal lateFee = lateBillPayment.getLateFee();

        BigDecimal newPaymentAmount = lateBillPayment.getBillPayment().getPaymentAmount().add(lateFee);
        LocalDate nextPaymentDate = getNextPaymentDateFromPayment(lateBillPayment.getBillPayment());
        nextScheduledPaymentMap.put(nextPaymentDate, newPaymentAmount);
    }

    public LateBillPayment buildLateBillPayment(BillPayment billPayment) {
        if(billPayment == null){
            throw new InvalidBillPaymentException("Found null payment criteria.");
        }
        final BigDecimal lateFee = new BigDecimal("25.00");
        return new LateBillPayment(billPayment.getDueDate(), lateFee, billPayment);
    }

    public void updateLateFee(LateBillPayment lateBillPayment, BigDecimal newLateFee) {

    }

    public BigDecimal calculateTotalAmountDue(LateBillPayment lateBillPayment) {
        return null;
    }

}
