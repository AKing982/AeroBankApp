package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.model.LateBillPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class LatePaymentMessageStrategy implements NotificationStrategy<LateBillPayment>
{
    private Logger LOGGER = LoggerFactory.getLogger(LatePaymentMessageStrategy.class);

    public LatePaymentMessageStrategy(){

    }

    @Override
    public StringBuilder buildMessage(LateBillPayment payment) {
        String messageFormat = "Your payment of $%s was due on %s. As of %s, your payment is %d days late. Late fee incurred: $%s.";
        String paymentAmount = payment.getBillPayment().getPaymentAmount().toString();
        String dueDate = payment.getBillPayment().getDueDate().toString();
        String currentDate = LocalDate.now().toString();
        long daysLate = ChronoUnit.DAYS.between(payment.getBillPayment().getDueDate(), payment.getBillPayment().getScheduledPaymentDate());
        BigDecimal lateFee = payment.getLateFee();
        String message = String.format(messageFormat, paymentAmount, dueDate, currentDate, daysLate, lateFee);
        LOGGER.info("Message: {}", message);
        return new StringBuilder(message);
    }
}
