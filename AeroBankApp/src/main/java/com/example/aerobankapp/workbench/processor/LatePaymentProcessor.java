package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.exceptions.IllegalDateException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidLatePaymentException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.billPayment.BillPaymentNotificationSender;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import com.example.aerobankapp.workbench.utilities.notifications.LatePaymentMessageStrategy;
import com.example.aerobankapp.workbench.utilities.notifications.LatePaymentNotificationSender;
import com.example.aerobankapp.workbench.utilities.notifications.NotificationStrategy;
import com.example.aerobankapp.workbench.utilities.notifications.SystemNotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeMap;

@Service
public class LatePaymentProcessor extends PaymentProcessor<LateBillPayment, ProcessedLatePayment> implements ScheduledPaymentProcessor<LateBillPayment>
{

    private LatePaymentNotificationSender latePaymentNotificationSender;
    private TreeMap<LocalDate, List<LateBillPayment>> latePayments = new TreeMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(LatePaymentProcessor.class);

    @Autowired
    public LatePaymentProcessor(ConfirmationNumberGenerator confirmationNumberGenerator, ReferenceNumberGenerator referenceNumberGenerator, LatePaymentNotificationSender latePaymentNotificationSender) {
        super(confirmationNumberGenerator, referenceNumberGenerator);
        this.latePaymentNotificationSender = latePaymentNotificationSender;
    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(LateBillPayment payment) {
        return null;
    }

    public int getTotalLatePayments(){
        return latePayments.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    public LateBillPayment buildLateBillPayment(BillPayment billPayment) {
        LateBillPayment lateBillPayment = new LateBillPayment();
        lateBillPayment.setBillPayment(billPayment);
        lateBillPayment.setOriginalDueDate(billPayment.getDueDate());
        lateBillPayment.setLateFee(calculateTotalAmountDue(lateBillPayment));
        return lateBillPayment;
    }

    public int calculateTotalDaysSinceDueDate(LocalDate paymentDate, LocalDate dueDate){
        return (int) ChronoUnit.DAYS.between(dueDate, paymentDate);
    }

    public BigDecimal calculateTotalAmountDue(final LateBillPayment lateBillPayment) {
        LocalDate paymentDate = lateBillPayment.getBillPayment().getScheduledPaymentDate();
        LocalDate dueDate = lateBillPayment.getBillPayment().getDueDate();
        BigDecimal paymentAmount = lateBillPayment.getBillPayment().getPaymentAmount();
        BigDecimal fees = BigDecimal.ZERO;
        if(paymentDate.isAfter(dueDate)){
            int totalDaysSinceDueDate = calculateTotalDaysSinceDueDate(paymentDate, dueDate);

            switch(totalDaysSinceDueDate){
                case 1:
                    fees = addFee(LatePaymentFees.ONE_DAY, fees);
                    return getPaymentAmountWithFee(paymentAmount, fees);
                case 2:
                    fees = addFee(LatePaymentFees.TWO_DAY, fees);
                    return getPaymentAmountWithFee(paymentAmount, fees);
                case 3:
                    fees = addFee(LatePaymentFees.THREE_DAY, fees);
                    return getPaymentAmountWithFee(paymentAmount, fees);
                case 7:
                    fees = addFee(LatePaymentFees.SEVEN_DAY, fees);
                    return getPaymentAmountWithFee(paymentAmount, fees);
                case 14:
                    fees = addFee(LatePaymentFees.TWO_WEEKS, fees);
                    return getPaymentAmountWithFee(paymentAmount, fees);
                default:
                    throw new UnsupportedOperationException("Unsupported number of days since due date");

            }
        }
        return fees.add(paymentAmount);
    }

    public BigDecimal addFee(final BigDecimal fee, final BigDecimal totalFees){
        return totalFees.add(fee);
    }

    public BigDecimal getPaymentAmountWithFee(final BigDecimal paymentAmount, final BigDecimal fee){
        return fee.add(paymentAmount);
    }


    public boolean sendLatePaymentNotification(final LateBillPayment lateBillPayment) {
        if(lateBillPayment == null){
            throw new InvalidLatePaymentException("unable to send latePayment notification due to null late payment object.");
        }
        validateLatePaymentCriteria(lateBillPayment);
        StringBuilder paymentMessage = latePaymentNotificationSender.createMessage(lateBillPayment);
        LOGGER.info("Payment Message: {}", paymentMessage.toString());
        AccountNotification accountNotification = AccountNotificationUtil.buildAccountNotificationModel(paymentMessage, lateBillPayment);
        return latePaymentNotificationSender.send(accountNotification);
    }

    public void validateLatePaymentCriteria(LateBillPayment lateBillPayment) {
        if(lateBillPayment.getBillPayment().getScheduledPaymentDate() == null ||
            lateBillPayment.getBillPayment().getDueDate() == null ||
            lateBillPayment.getBillPayment().getPaymentAmount() == null ||
            lateBillPayment.getBillPayment().getPaymentAmount().compareTo(BigDecimal.ZERO) == 0 ||
         lateBillPayment.getBillPayment().getPayeeName() == null ||
        lateBillPayment.getBillPayment().getAccountCode() == null){
            throw new InvalidLatePaymentException("Found Null Late Payment Criteria: " + lateBillPayment.getBillPayment().toString());
        }
    }

    public boolean isBillPaymentLate(final BillPayment billPayment) {
        validateBillPayment(billPayment);

        if (billPayment.getScheduledPaymentDate().isAfter(billPayment.getDueDate())) {
            return true;
        }
        return false;
    }

    public ProcessedLatePayment processLatePayment(final BillPayment payment){
        validateBillPayment(payment);
        isBillPaymentLate(payment);
        LateBillPayment lateBillPayment = buildLateBillPayment(payment);

        return processSinglePayment(lateBillPayment);
    }

    private void validateBillPayment(final BillPayment billPayment) {
        if(billPayment == null){
            throw new InvalidBillPaymentException("Bill payment is null");
        }
        if(billPayment.getPaymentAmount() == null){
            throw new InvalidBillPaymentException("Bill payment amount is null");
        }
        if(billPayment.getDueDate() == null){
            throw new IllegalDateException("Due date is null");
        }
    }

    private void validateLatePayment(LateBillPayment lateBillPayment){
        if(lateBillPayment == null){
            throw new InvalidLatePaymentException("Late payment is null");
        }

        validateLatePaymentCriteria(lateBillPayment);
    }

    @Override
    public List<ProcessedLatePayment> processPayments(List<LateBillPayment> payments) {
        return List.of();
    }

    @Override
    public ProcessedLatePayment processSinglePayment(LateBillPayment payment) {
        // Validate the late payment
        validateLatePayment(payment);

        // Calculate the Late Fee Amount
        BigDecimal calculatedLateFee = calculateTotalAmountDue(payment);

        // Generate the Confirmation and ReferenceNumber
        ConfirmationNumber confirmationNumber = confirmationNumberGenerator.generateConfirmationNumber();
        ReferenceNumber referenceNumber = referenceNumberGenerator.generateReferenceNumber();

        // Update the transaction status

        // Send notification to the user

        // Return the Processed Late payment
        return null;
    }
}
