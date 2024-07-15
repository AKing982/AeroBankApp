package com.example.aerobankapp.aspect;

import com.example.aerobankapp.dto.TransferDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransferControllerLoggingAspect
{
    private Logger LOGGER = LoggerFactory.getLogger(TransferControllerLoggingAspect.class);

    @Around("execution(* com.example.aerobankapp.controllers.TransferController.*(..))")
    public Object logAroundAllMethods(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Started Execution of " + methodName);
        Object result = null;
        try{
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            LOGGER.info("Finished Execution of " + methodName + " in " + executionTime + " ms");

        }catch(Exception e){
            LOGGER.error("Error executing " + methodName, e);
            throw e;
        }
        return result;
    }

    @Around("execution(* com.example.aerobankapp.controllers.TransferController.saveTransfer(..))")
    public Object logAroundSaveTransferMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        Object[] methodsArgs = joinPoint.getArgs();
        if(methodsArgs != null && methodsArgs.length > 0 && methodsArgs[0] instanceof TransferDTO){
            TransferDTO transferDTO = (TransferDTO) methodsArgs[0];
            LOGGER.info("Saving TransferDTO: {}", transferDTO);
        }

        LOGGER.info("Started Execution of " + methodName);
        Object result = null;
        try{
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            LOGGER.info("Finished Execution of " + methodName + " in " + executionTime + " ms");
        }catch(Exception e){
            LOGGER.error("Error executing " + methodName, e);
            throw e;
        }
        return result;
    }


}
