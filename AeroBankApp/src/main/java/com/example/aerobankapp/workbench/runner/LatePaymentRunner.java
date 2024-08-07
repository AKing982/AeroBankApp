package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.processor.LatePaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LatePaymentRunner implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(LatePaymentRunner.class);
    private final LatePaymentProcessor latePaymentProcessor;

    @Autowired
    public LatePaymentRunner(LatePaymentProcessor latePaymentProcessor) {
        this.latePaymentProcessor = latePaymentProcessor;
    }

    @Override
    public void run() {

    }
}
