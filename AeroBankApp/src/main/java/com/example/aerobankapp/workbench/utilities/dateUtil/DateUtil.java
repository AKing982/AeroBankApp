package com.example.aerobankapp.workbench.utilities.dateUtil;

import java.time.LocalDate;

public class DateUtil
{
    private static LocalDate getNowDate(){
        return LocalDate.now();
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
