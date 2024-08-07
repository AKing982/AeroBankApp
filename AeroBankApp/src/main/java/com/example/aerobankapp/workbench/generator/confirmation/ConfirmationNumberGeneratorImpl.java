package com.example.aerobankapp.workbench.generator.confirmation;

import com.example.aerobankapp.model.ConfirmationNumber;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ConfirmationNumberGeneratorImpl implements ConfirmationNumberGenerator
{
    private final SecureRandom secureRandom = new SecureRandom();
    private final int CONFIRMATION_LENGTH = 7;
    private final int CONFIRMATION_BOUND = (int)Math.pow(10, CONFIRMATION_LENGTH);

    @Override
    public ConfirmationNumber generateConfirmationNumber() {
        int confirmationNumber = secureRandom.nextInt(CONFIRMATION_BOUND);
        while(Integer.toString(confirmationNumber).length() != CONFIRMATION_LENGTH){
            confirmationNumber = secureRandom.nextInt(CONFIRMATION_BOUND);
        }
        return new ConfirmationNumber(confirmationNumber);
    }
}
