package com.example.aerobankapp.workbench.generator.confirmation;

import com.example.aerobankapp.model.ConfirmationNumber;

import java.security.SecureRandom;

public class ConfirmationNumberGeneratorImpl implements ConfirmationNumberGenerator
{
    private final SecureRandom secureRandom = new SecureRandom();
    private final int CONFIRMATION_LENGTH = 7;
    private final int CONFIRMATION_BOUND = (int)Math.pow(10, CONFIRMATION_LENGTH);

    @Override
    public ConfirmationNumber generateConfirmationNumber() {
        return null;
    }

    @Override
    public boolean isUniqueConfirmation(ConfirmationNumber number) {
        return false;
    }
}
