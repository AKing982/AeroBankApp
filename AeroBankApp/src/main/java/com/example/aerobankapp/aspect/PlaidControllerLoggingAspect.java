package com.example.aerobankapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PlaidControllerLoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidControllerLoggingAspect.class);

    @Around("execution(* com.example.aerobankapp.controllers.PlaidController.*(..))")
    public Object logAroundAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        LOGGER.info("Started Execution of " + methodName);
        Object result = null;
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            LOGGER.info("Finished Execution of " + methodName + " in " + executionTime + " ms");
        } catch (Exception e) {
            LOGGER.error("Error executing " + methodName, e);
            throw e;
        }
        return result;
    }

    @Before("execution(* com.example.aerobankapp.controllers.PlaidController.getTransactions(..))")
    public void logBeforeTransaction(JoinPoint joinPoint)
    {
        Object[] args = joinPoint.getArgs();
        LOGGER.info("getTransactions called with userId: {}, startDate: {}, endDate: {}", args[0], args[1], args[2]);
    }
}
