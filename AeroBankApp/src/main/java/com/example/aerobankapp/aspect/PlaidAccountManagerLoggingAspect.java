package com.example.aerobankapp.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PlaidAccountManagerLoggingAspect
{
    private static Logger LOGGER = LoggerFactory.getLogger(PlaidAccountManagerLoggingAspect.class);

    @Around("execution(* ")
}
