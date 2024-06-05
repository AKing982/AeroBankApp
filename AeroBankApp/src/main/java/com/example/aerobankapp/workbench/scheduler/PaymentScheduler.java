package com.example.aerobankapp.workbench.scheduler;

import com.example.aerobankapp.exceptions.IllegalDateException;
import com.example.aerobankapp.exceptions.IllegalScheduleCriteriaException;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;

import java.time.LocalDate;
import java.util.Optional;

public abstract class PaymentScheduler<T>
{
    public abstract Optional<LocalDate> getNextPaymentDate(T t);
    public abstract Optional<LocalDate> getPreviousPaymentDate(T t);

    protected Optional<LocalDate> calculateNextPaymentDate(LocalDate currentDate, ScheduleFrequency frequency) {
        validateNextPaymentDateCriteria(currentDate, frequency);
        switch(frequency){
            case MONTHLY -> {
                return buildNextPaymentDateByMonth(currentDate);
            }
            case WEEKLY -> {
                return buildNextPaymentDateByWeek(currentDate);
            }
            case BI_WEEKLY -> {
                return buildNextPaymentDateByBiWeekly(currentDate);
            }
        }
        return Optional.ofNullable(currentDate);
    }

    protected Optional<LocalDate> buildNextPaymentDateByBiWeekly(LocalDate date){
        if(date != null){
            return Optional.of(date.plusWeeks(2));
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    protected Optional<LocalDate> buildNextPaymentDateByWeek(LocalDate date){
        if(date != null){
            return Optional.of(date.plusWeeks(1));
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    protected Optional<LocalDate> buildNextPaymentDateByMonth(LocalDate date){
        if(date != null){
            return Optional.of(date.plusMonths(1));
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    protected void validateNextPaymentDateCriteria(LocalDate date, ScheduleFrequency frequency){
        if(date == null){
            throw new IllegalDateException("Null Date criteria caught. Unable to calculate next payment date.");
        }
        else if(frequency == null){
            throw new IllegalScheduleCriteriaException("Null ScheduleFrequency found. Unable to calculate next payment date.");
        }
    }



}
