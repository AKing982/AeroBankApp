package com.example.aerobankapp.aspect;

import com.example.aerobankapp.model.PlaidAccount;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class PlaidAccountManagerLoggingAspect
{
    private static Logger LOGGER = LoggerFactory.getLogger(PlaidAccountManagerLoggingAspect.class);

    @Around("execution(* com.example.aerobankapp.workbench.plaid.*.getPlaidAccountsSetFromResponse(..)) " +
            "|| execution(* com.example.aerobankapp.workbench.plaid.*.getAllAccounts(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        LOGGER.info("Entering Method: " + joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;
        LOGGER.info("Method: " + joinPoint.getSignature().getName() + " executed in " + elapsed + " ms");
        return result;
    }

    @AfterReturning(pointcut="execution(* com.example.aerobankapp.workbench.plaid.*.getPlaidAccountsSetFromResponse(..))",
    returning="result")
    public void logAfterGettingPlaidAccountsSetFromResponse(JoinPoint joinPoint, Object result)
    {
        Set<PlaidAccount> plaidAccountSet = (Set<PlaidAccount>) result;
        for(PlaidAccount plaidAccount : plaidAccountSet)
        {
            LOGGER.info("Plaid Account created: {}", plaidAccount.toString());
        }
    }

    @AfterThrowing(pointcut="execution(* com.example.aerobankapp.workbench.plaid.*.getAllAccounts(..))", throwing="ex")
    public void logAfterThrowingAllMethods(Exception ex)
    {
        LOGGER.error("Exception in method getAllAccounts", ex);
    }
}
