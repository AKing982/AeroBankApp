package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.billPayment.BillPaymentNotificationSender;
import com.example.aerobankapp.workbench.utilities.notifications.SystemNotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Service
public class LatePaymentProcessor implements PaymentProcessor<LateBillPayment, ProcessedBillPayment>, ScheduledPaymentProcessor<LateBillPayment>
{

    private SystemNotificationSender<AccountNotification, LateBillPayment> accountNotificationSender;
    private TreeMap<LocalDate, List<LateBillPayment>> latePayments = new TreeMap<>();

    @Autowired
    public LatePaymentProcessor(SystemNotificationSender<AccountNotification, LateBillPayment> accountNotificationSender){
        this.accountNotificationSender = accountNotificationSender;
    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(LateBillPayment payment) {
        return null;
    }

    public BigDecimal getTotalLatePayments(){
        return null;
    }

    public void processLatePayment(LateBillPayment lateBillPayment) {

    }

    public LateBillPayment buildLateBillPayment(BillPayment billPayment) {
        return null;
    }

    public void updateLateFee(LateBillPayment lateBillPayment, BigDecimal newLateFee) {

    }

    public BigDecimal calculateTotalAmountDue(LateBillPayment lateBillPayment) {
        return null;
    }


    public boolean sendLatePaymentNotification(LateBillPayment lateBillPayment) {
        return false;
    }

    public boolean isBillPaymentLate(BillPayment billPayment) {
        return false;
    }

    @Override
    public List<ProcessedBillPayment> processPayments(List<LateBillPayment> payments) {
        return List.of();
    }

    @Override
    public ProcessedBillPayment processSinglePayment(LateBillPayment payment) {
        return null;
    }
}
