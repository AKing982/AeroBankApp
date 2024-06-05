package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.ProcessedBillPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class NotificationMessageBuilder
{
    public NotificationMessageBuilder(){

    }

    public StringBuilder buildProcessedPaymentNotification(ProcessedBillPayment payment) {
        StringBuilder sb = new StringBuilder();
        int acctID = payment.getBillPayment().getAccountCode().getSequence();
        BigDecimal paymentAmount = payment.getBillPayment().getPaymentAmount();
        LocalDate nextPaymentDate = payment.getNextPaymentDate();

        sb.append("Payment of $")
                .append(paymentAmount)
                .append(" has been processed for account ")
                .append(acctID)
                .append(". Next payment is scheduled on ")
                .append(nextPaymentDateAsString(nextPaymentDate))
                .append(".");

        return sb;
    }

    private String nextPaymentDateAsString(LocalDate date) {
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A";
    }
}
