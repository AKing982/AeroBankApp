package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.MissedBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Component
public class MissedPaymentProcessor extends PaymentProcessor<MissedBillPayment, ProcessedBillPayment>
{
    private TreeMap<LocalDate, List<MissedBillPayment>> missedPaymentsTree = new TreeMap<LocalDate, List<MissedBillPayment>>();

    public MissedPaymentProcessor(ConfirmationNumberGenerator confirmationNumberGenerator) {
        super(confirmationNumberGenerator);
    }

    @Override
    public List<ProcessedBillPayment> processPayments(List<MissedBillPayment> payments) {
        return List.of();
    }

    @Override
    public ProcessedBillPayment processSinglePayment(MissedBillPayment payment) {
        return null;
    }

    public void addMissedPayment(LocalDate date, MissedBillPayment payment) {

    }

    public void removeProcessedPayment(LocalDate date, ProcessedBillPayment payment) {

    }

    public List<MissedBillPayment> getMissedPayments() {
        return null;
    }

    private boolean checkForMissedPayments(){
        return false;
    }

    public boolean isPaymentMissed(BillPayment billPayment){
        return false;
    }

    public List<MissedBillPayment> getMissedPaymentsByDate(LocalDate date) {
        return null;
    }


}
