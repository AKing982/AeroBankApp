package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.scheduler.jobs.BillPaymentJob;
import com.example.aerobankapp.workbench.billPayment.BillPaymentJobManager;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
public class BillPaymentScheduler extends PaymentScheduler<BillPayment> {

    private final BillPaymentDataManager billPaymentDataManager;
    private final BillPaymentJobManager billPaymentJobManager;
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentScheduler.class);
    private TreeMap<LocalDate, List<BillPayment>> scheduledPayments = new TreeMap<>();

    @Autowired
    public BillPaymentScheduler(BillPaymentDataManager billPaymentDataManager,
                                BillPaymentJobManager billPaymentJobManager) {
        this.billPaymentDataManager = billPaymentDataManager;
        this.billPaymentJobManager = billPaymentJobManager;
    }

    public Collection<BillPaymentEntity> getBillPaymentsFromDB(){
        return billPaymentDataManager.findAllBillPayments();
    }

    public Collection<BillPaymentScheduleEntity> getBillPaymentSchedulesFromDB(){
        return billPaymentDataManager.findAllBillPaymentSchedules();
    }



    public boolean schedulePayment(BillPayment billPayment) {
        // Validate the bill payment
        validateBillPayment(billPayment);

        // If the paymentID is valid
        LocalDate scheduledPaymentDate = billPayment.getScheduledPaymentDate();
        Long paymentID = billPayment.getPaymentID();
        if(scheduledPaymentDate == null)
        {
            if(paymentID > 0)
            {
                // Fetch the scheduled payment from the database
                Optional<LocalDate> scheduledPaymentOptional = billPaymentDataManager.findLastScheduledPaymentDateInScheduleTableByPaymentID(paymentID);
                if(scheduledPaymentOptional.isPresent())
                {
                    LocalDate paymentDate = scheduledPaymentOptional.get();
                    return billPaymentJobManager.executeScheduleJobTask(billPayment);
                }
            }
            else
            {
                throw new InvalidBillPaymentIDException("Bill payment ID is not valid");
            }
        }
        return billPaymentJobManager.executeScheduleJobTask(billPayment);
    }



    public boolean reschedulePayment(String jobId, Date newDate)
    {
        return false;
    }

    public TreeMap<LocalDate, BillPayment> getScheduledPaymentsForPeriod(LocalDate startDate, LocalDate endDate){
        return null;
    }

    public Optional<LocalDate> calculateNextDueDate(LocalDate previousDueDate, ScheduleFrequency frequency) {
        if(previousDueDate == null)
        {
            throw new IllegalDateException("The previous due date cannot be null");
        }

        if(frequency == null)
        {
            throw new IllegalArgumentException("The schedule frequency cannot be null");
        }

        LocalDate nextDueDate;
        switch(frequency)
        {
            case MONTHLY ->
            {
                nextDueDate = previousDueDate.plusMonths(1);
                int lastDayOfMonth = nextDueDate.lengthOfMonth();
                if (previousDueDate.getDayOfMonth() > lastDayOfMonth)
                {
                    nextDueDate = nextDueDate.withDayOfMonth(lastDayOfMonth);
                }
                else
                {
                    nextDueDate = nextDueDate.withDayOfMonth(previousDueDate.getDayOfMonth());
                }
            }
            case WEEKLY -> nextDueDate = getNextIncrementedWeek(previousDueDate, 1);
            case BI_WEEKLY -> nextDueDate = getNextIncrementedWeek(previousDueDate, 2);

            case TRI_WEEKLY -> nextDueDate = getNextIncrementedWeek(previousDueDate, 3);
            default -> throw new IllegalArgumentException("Unsupported schedule frequency: " + frequency);
        }
        return Optional.of(nextDueDate);
    }

    private LocalDate getNextIncrementedWeek(LocalDate previousDueDate, int numWeeks){
        return previousDueDate.plusWeeks(numWeeks);
    }


    public TreeMap<String, LocalDate> getNextPaymentDetails(final BillPayment payment)
    {
        TreeMap<String, LocalDate> nextPaymentDetails = new TreeMap<>();
        if(payment == null)
        {
            throw new InvalidBillPaymentException("The payment cannot be null");
        }

        LocalDate dueDate = payment.getDueDate();
        LocalDate paymentDate = payment.getScheduledPaymentDate();
        if(dueDate == null && paymentDate == null)
        {
            throw new IllegalDateException("The payment date and due date cannot be null");
        }

        if(dueDate == null)
        {
            // Fetch the latest due date using the paymentID
            LocalDate latestDueDate = billPaymentDataManager.findLastDueDateByPaymentID(payment.getPaymentID());

            // Determine the next due date
            Optional<LocalDate> nextDueDate = calculateNextDueDate(latestDueDate, payment.getScheduleFrequency());
            nextDueDate.ifPresent(localDate -> nextPaymentDetails.put("nextDueDate", localDate));

            // Fetch the next scheduled payment date
            Optional<LocalDate> nextPaymentDate = getNextPaymentDate(payment);
            nextPaymentDate.ifPresent((LocalDate localDate) -> nextPaymentDetails.put("nextPaymentDate", localDate));
        }

        if(paymentDate == null)
        {
            // Fetch the latest scheduled payment using the paymentID
            Optional<LocalDate> latestScheduledPaymentDate = billPaymentDataManager.findLastScheduledPaymentDateByPaymentID(payment.getPaymentID());
            if(latestScheduledPaymentDate.isPresent())
            {
                LocalDate latestPaymentDate = latestScheduledPaymentDate.get();
                Optional<LocalDate> nextScheduledPaymentDate = calculateNextPaymentDate(latestPaymentDate, payment.getScheduleFrequency());
                nextScheduledPaymentDate.ifPresent((LocalDate localDate) -> nextPaymentDetails.put("nextPaymentDate", localDate));

                Optional<LocalDate> nextDueDate = calculateNextDueDate(dueDate, payment.getScheduleFrequency());
                nextDueDate.ifPresent((LocalDate localDate) -> nextPaymentDetails.put("nextDueDate", localDate));
            }
            else
            {
                Optional<LocalDate> latestPaymentDateInScheduleTable = billPaymentDataManager.findLastScheduledPaymentDateInScheduleTableByPaymentID(payment.getPaymentID());
                if(latestPaymentDateInScheduleTable.isPresent())
                {
                    LocalDate latestPaymentDate = latestPaymentDateInScheduleTable.get();
                    Optional<LocalDate> nextScheduledPaymentDate = calculateNextPaymentDate(latestPaymentDate, payment.getScheduleFrequency());
                    nextScheduledPaymentDate.ifPresent((LocalDate localDate) -> nextPaymentDetails.put("nextPaymentDate", localDate));

                    Optional<LocalDate> nextDueDate = calculateNextDueDate(dueDate, payment.getScheduleFrequency());
                    nextDueDate.ifPresent((LocalDate localDate) -> nextPaymentDetails.put("nextDueDate", localDate));
                }
                else
                {
                    throw new ScheduledPaymentNotFoundException("The scheduled payment date could not be found in the database.");
                }
            }
        }
        return nextPaymentDetails;
    }


    @Override
    public Optional<LocalDate> getNextPaymentDate(BillPayment payment) {
        if(payment == null)
        {
            throw new InvalidBillPaymentException("Unable to retrieve next payment date from null bill payment.");
        }

        ScheduleFrequency frequency = payment.getScheduleFrequency();
        LocalDate paymentDate = payment.getScheduledPaymentDate();
        LocalDate dueDate = payment.getDueDate();
        if(paymentDate == null)
        {
            if(dueDate != null)
            {
                Optional<LocalDate> nextPaymentDateOptional = calculateNextPaymentDate(dueDate, frequency);
                if(nextPaymentDateOptional.isPresent())
                {
                    return nextPaymentDateOptional;
                }
                else
                {
                    throw new SchedulePaymentDateException("The next payment date could not be calculated because of missing payment date and due date.");
                }
            }
            else
            {
                throw new InvalidBillPaymentParametersException("Payment date and due date cannot be null.");
            }
        }
        LOGGER.info("Calculating next payment date for payment {}", payment);
        return calculateNextPaymentDate(paymentDate, frequency);
    }

    @Override
    public Optional<LocalDate> getPreviousPaymentDate(final BillPayment payment) {
        if (payment == null) {
            throw new InvalidBillPaymentException("Unable to retrieve previous payment date from null bill payment.");
        }

        if (payment.getPaymentID() > 0)
        {
            // Else if the last payment is null or empty
            return billPaymentDataManager.findLastScheduledPaymentDateByPaymentID(payment.getSchedulePaymentID());
        }
        else
        {
            throw new InvalidBillPaymentIDException("Bill payment ID must be greater than zero.");
        }
    }

    private LocalDate getPaymentDate(BillPayment payment) {
        return payment.getScheduledPaymentDate();
    }

    private LocalDate getPaymentDueDate(BillPayment payment) {
        return payment.getDueDate();
    }

    private void validateBillPayment(BillPayment billPayment){
        if(billPayment == null){
            throw new InvalidBillPaymentException("Bill payment cannot be null.");
        }
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
