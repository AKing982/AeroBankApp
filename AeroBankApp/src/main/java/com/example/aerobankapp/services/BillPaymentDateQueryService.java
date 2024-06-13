package com.example.aerobankapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BillPaymentDateQueryService extends AbstractCustomQueryService<LocalDate>
{

    public BillPaymentDateQueryService(){

    }

    @Transactional
    public Optional<LocalDate> findPaymentDueDateById(Long paymentId) {
        String jpql = "SELECT bps.paymentDueDate FROM BillPaymentEntity bp JOIN bp.paymentSchedule bps WHERE bp.paymentID = ?1";
        return executeQuery(jpql, paymentId);
    }

    @Transactional
    public Optional<LocalDate> findScheduledPaymentDateById(Long paymentId) {
        String jpql = "SELECT bph.lastPayment FROM BillPaymentEntity bp JOIN bp.paymentSchedule bps JOIN bps.billPaymentHistory bph WHERE bp.paymentID = ?1";
        return executeQuery(jpql, paymentId);
    }

    @Transactional
    public Optional<LocalDate> findScheduledPaymentByScheduleTableById(Long paymentId) {
        String jpql = "SELECT bps.scheduledPaymentDate FROM BillPaymentEntity bp JOIN bp.paymentSchedule bps WHERE bp.paymentID = ?1";
        return executeQuery(jpql, paymentId);
    }
}
