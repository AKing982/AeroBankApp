package com.example.aerobankapp.workbench.utilities.logging;

import com.example.aerobankapp.messages.CommonPhrases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AeroLogger
{
    private static Logger LOGGER;

    public AeroLogger(Class<?> clazz)
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callingClassName = stackTrace[2].getClassName();
        LOGGER = LoggerFactory.getLogger(callingClassName);
    }

    public Logger getLogger()
    {
        return LOGGER;
    }

    public Logger getLogger(String name)
    {
        return LoggerFactory.getLogger(name);
    }

    public void info(String msg, Object var)
    {
        LOGGER.info(msg, var);
    }

    public void info(String msg)
    {
        LOGGER.info(msg);
    }

    public void warn(String msg)
    {
        LOGGER.warn(msg);
    }

    public void error(String msg, Object var)
    {
        LOGGER.error(msg, var);
    }

    public void error(String msg)
    {
        LOGGER.error(msg);
    }

    public void handleException(Exception e)
    {
       handleException(e, " ");
    }

    public void handleException(Exception e, String info)
    {
        Date today = new Date();
        StringBuilder sb = new StringBuilder();

        //TODO:
        sb.append(CommonPhrases.NL_NL);
        sb.append("======================================================");
        sb.append(CommonPhrases.NL);
        sb.append(today.toString());
        sb.append(CommonPhrases.NL);
        sb.append(e.getClass());
        sb.append(CommonPhrases.NL);
        if(!info.isEmpty()){
            sb.append(info);
            sb.append(CommonPhrases.NL);
        }
        sb.append("=======================================================");
        sb.append(CommonPhrases.NL);
        sb.append(e.getMessage());
    }

    public void log()
    {
        Date today = new Date();
        StringBuilder loggedData = new StringBuilder();
        loggedData.append("=================================================");
        loggedData.append(today);
        loggedData.append("=================================================");
        //loggedData.append(LOGGER.info());
    }
}
