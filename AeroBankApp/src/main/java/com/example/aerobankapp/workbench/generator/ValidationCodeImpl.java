package com.example.aerobankapp.workbench.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ValidationCodeImpl implements ValidationCode
{
    private static final SecureRandom random = new SecureRandom();
    private static final int CODE_LENGTH = 6;

    @Override
    public String generateValidationCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));  // Append a random digit (0-9)
        }
        return sb.toString();
    }
}
