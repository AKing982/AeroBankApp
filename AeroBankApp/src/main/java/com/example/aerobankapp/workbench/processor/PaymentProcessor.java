package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class PaymentProcessor<T, R>
{
    protected ConfirmationNumberGenerator confirmationNumberGenerator;
    protected ReferenceNumberGenerator referenceNumberGenerator;

    @Autowired
    public PaymentProcessor(ConfirmationNumberGenerator confirmationNumberGenerator,
                            ReferenceNumberGenerator referenceNumberGenerator){
        this.confirmationNumberGenerator = confirmationNumberGenerator;
        this.referenceNumberGenerator = referenceNumberGenerator;
    }

    abstract List<R> processPayments(List<T> payments);
    abstract R processSinglePayment(T payment);
}
