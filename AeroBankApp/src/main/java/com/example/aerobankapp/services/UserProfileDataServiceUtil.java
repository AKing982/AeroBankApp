package com.example.aerobankapp.services;

import com.mchange.v2.cfg.PropertiesConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserProfileDataServiceUtil
{
    private static Logger LOGGER = LoggerFactory.getLogger(UserProfileDataServiceUtil.class);

    public static String getFormattedLastLoginDate(String lastLogin) throws ParseException
    {
        if(lastLogin == null || lastLogin.isEmpty())
        {
            return "";
        }

        try
        {
            Time lastLoginDate = getLastLoginAsLocalTime(lastLogin);
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
            return timeFormat.format(lastLoginDate);

        }catch(Exception e)
        {
            LOGGER.error("There was an error while getting the last login date", e);
            throw e;
        }
    }

    public static Time getLastLoginAsLocalTime(String lastLogin) throws ParseException
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("h:mm:ss a");
            long ms = format.parse(lastLogin).getTime();
            return new Time(ms);

        }catch(ParseException e)
        {
            LOGGER.error("There was an error while getting the last login date", e);
            throw e;
        }
    }
}
