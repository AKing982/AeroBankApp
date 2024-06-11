package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidDateException;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class BillPaymentScheduler extends PaymentScheduler<BillPayment> {

    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentScheduler.class);
    private TreeMap<LocalDate, List<BillPayment>> scheduledPayments = new TreeMap<>();

    public BillPaymentScheduler() {

    }

    public void cancelPayment(BillPayment billPayment) {

    }

    public void schedulePayment(BillPayment billPayment) {

    }

    public void reschedulePayment(BillPayment billPayment, LocalDate newDate) {

    }

    public void rescheduleLatePayment(LateBillPayment latePayment, LocalDate newDate) {

    }

    public boolean isPaymentScheduled(BillPayment billPayment) {
        return false;
    }

    public List<BillPayment> getScheduledPaymentsForPeriod(LocalDate startDate, LocalDate endDate){
        return null;
    }

    private void scheduleNextPayment(BillPayment payment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap) {
      //  LocalDate nextPaymentDate = getNextPaymentDateFromPayment(payment);
       // nextScheduledPaymentMap.put(nextPaymentDate, payment.getPaymentAmount());
    }

    public boolean validatePaymentDatePriorDueDate(BillPayment payment, LocalDate newDate) {
        return false;
    }


    @Override
    public Optional<LocalDate> getNextPaymentDate(BillPayment payment) {
        if(payment == null){
            throw new InvalidBillPaymentException("Unable to retrieve next payment date from null bill payment.");
        }
//        LocalDate dueDate = payment.getDueDate();
//        ScheduleFrequency frequency = payment.getScheduleFrequency();
//        LocalDate paymentDate = payment.getScheduledPaymentDate();
//        LOGGER.info("Due Date: {}, Frequency: {}, Payment Date: {}", dueDate, frequency, paymentDate);
//
//        if(paymentDate == null){
//            LOGGER.info("Calculating next payment date by due date");
//            return calculateNextPaymentDate(dueDate, frequency);
//        }
//        LOGGER.info("Calculating next payment date by payment date.");
//        return calculateNextPaymentDate(paymentDate, frequency);
        return null;
    }

    @Override
    public Optional<LocalDate> getPreviousPaymentDate(BillPayment payment) {
       return Optional.empty();
    }

    private LocalDate getPaymentDate(BillPayment payment) {
        return payment.getScheduledPaymentDate();
    }

    private LocalDate getPaymentDueDate(BillPayment payment) {
        return payment.getDueDate();
    }

    public boolean validatePaymentDatePriorDueDate(BillPayment payment){
        LocalDate paymentDate = getPaymentDate(payment);
        LocalDate dueDate = getPaymentDueDate(payment);
        if(paymentDate == null || dueDate == null){
            LOGGER.error("There was an error validating the Payment Date due to null date parameters: Payment Date: {}, Due Date: {}", paymentDate, dueDate);
            throw new InvalidDateException("Invalid Payment Date found");
        }
        return paymentDate.isBefore(dueDate);
    }
}