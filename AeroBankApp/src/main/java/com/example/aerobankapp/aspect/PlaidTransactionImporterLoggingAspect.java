package com.example.aerobankapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PlaidTransactionImporterLoggingAspect
{
    private Logger LOGGER = LoggerFactory.getLogger(PlaidTransactionImporterLoggingAspect.class);

    @Before("execution(* com.example.aerobankapp.workbench.plaid.PlaidTransactionImporter.saveProcessedPlaidTransactions(..))")
    public void logBeforeProcessing(JoinPoint joinPoint)
    {
        Object[] args = joinPoint.getArgs();
        LOGGER.info("saveProcessedPlaidTransactions called with plaidTransactionImport: {}", args[0]);
    }



}
