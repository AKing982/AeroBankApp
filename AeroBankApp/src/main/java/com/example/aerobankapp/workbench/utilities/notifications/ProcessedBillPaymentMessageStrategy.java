package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.model.ProcessedBillPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProcessedBillPaymentMessageStrategy implements NotificationStrategy<ProcessedBillPayment>
{


    @Override
    public StringBuilder buildMessage(ProcessedBillPayment payment) {
        StringBuilder message = new StringBuilder();
        message.append("Payment of $")
                .append(getPaymentAmount(payment))
                .append(" has been processed for account ")
                .append(getAccountCodeString(payment))
                .append(". Next payment is scheduled on ")
                .append(nextPaymentDateAsString(payment.getNextPaymentDate()))
                .append(".");
        return message;
    }

    private String nextPaymentDateAsString(LocalDate date){
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A";
    }

    private String getAccountCodeString(ProcessedBillPayment payment) {
        return payment.getBillPayment().getAccountCode().toString();
    }

    private BigDecimal getPaymentAmount(ProcessedBillPayment payment){
        return payment.getBillPayment().getPaymentAmount();
    }
}
