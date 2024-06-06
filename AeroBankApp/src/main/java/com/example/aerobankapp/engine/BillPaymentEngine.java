package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class BillPaymentEngine
{


    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngine.class);


    public BillPaymentEngine(){
    }

    //TODO: To Implement 6/3/24
    /**
     * Automatically pays the given list of bill payments.
     *
     * @param billPayments The list of bill payments to auto-pay.
     * @return A list of processed bill payments.
     * @throws NonEmptyListRequiredException if the billPayments list is empty.
     * @throws InvalidBillPaymentException if a null payment is found in the list when there is only one payment.
     */

    public List<ProcessedBillPayment> autoPayBills(final List<BillPayment> billPayments) {
        return null;
    }


    public boolean paymentVerification(final ProcessedBillPayment processedBillPayment, final BillPaymentHistory billPaymentHistory) {
        return false;
    }

    private void validatePaymentWithDB(Long paymentID){

    }


    public void processOnTimePayment(BillPayment billPayment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap) {

    }

    private BigDecimal calculateNewBalance(BillPayment billPayment){
       return null;
    }

    private void updateAccountAndPostProcessing(BillPayment payment, BigDecimal newBalance){

    }

    private void updateBillPaymentHistory(final BillPaymentHistory billPaymentHistory){


    }


    //TODO: To implement 6/3/24
    public void processRegularBillStatements(TreeMap<LocalDate, BillPayment> billPayments) {

    }

    /**
     * This method will update the account balance, account details
     * @param newBalance
     * @param acctID
     */
    public void postProcessingUpdate(BigDecimal newBalance, int acctID, BigDecimal prevBalance){

    }

    public void postProcessingUpdateBalanceHistory(BigDecimal newBalance, BigDecimal adjusted, BigDecimal prevBalance, int acctID){

    }


    public boolean sendProcessedPaymentNotification(ProcessedBillPayment billPayment) {
        return false;
    }


    public void handleDailyPayments(TreeMap<BillPayment, BillPaymentSchedule> billPayments) {

    }


    public void handleWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> weeklyPayments) {

    }


    public void handleMonthlyPayments(TreeMap<BillPayment, BillPaymentSchedule> monthlyPayments) {

    }


    public void handleBiWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> biWeeklyPayments) {

    }

    /**
     * Handles missed payments by returning a list of processed bill payments.
     *
     * @param lateBillPayments The list of late bill payments.
     * @return The list of processed bill payments.
     */

    public List<ProcessedBillPayment> handleMissedPayments(List<LateBillPayment> lateBillPayments) {
        return List.of();
    }
}
