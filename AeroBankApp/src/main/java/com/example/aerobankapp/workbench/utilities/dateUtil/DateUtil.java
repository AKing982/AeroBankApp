package com.example.aerobankapp.workbench.utilities.dateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class DateUtil
{
    private static LocalDate getNowDate(){
        return LocalDate.now();
    }


    public static LocalDate getStartOfCurrentWeek(){
        return getNowDate().with(ChronoField.DAY_OF_WEEK, 1);
    }

    public static LocalDate getEndOfCurrentWeek(){
        return getNowDate().with(ChronoField.DAY_OF_WEEK, 7);
    }

    private static int getMonth(){
        return getNowDate().getMonthValue();
    }

    private static int getYear(){
        return getNowDate().getYear();
    }

    public static LocalDate getCurrentMonthStartDate(){
        return LocalDate.of(getYear(), getMonth(), 1);
    }

    public static LocalDate getCurrentMonthEndDate(){
        LocalDate lastDay = getNowDate().withDayOfMonth(getNowDate().lengthOfMonth());
        return LocalDate.of(getYear(), getMonth(), lastDay.getDayOfMonth());
    }
}
