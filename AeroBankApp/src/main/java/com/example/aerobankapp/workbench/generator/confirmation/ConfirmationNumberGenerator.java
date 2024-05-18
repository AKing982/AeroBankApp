package com.example.aerobankapp.workbench.generator.confirmation;

import com.example.aerobankapp.model.ConfirmationNumber;

public interface ConfirmationNumberGenerator
{
    ConfirmationNumber generateConfirmationNumber();

    boolean isUniqueConfirmation(ConfirmationNumber number);
}
