package com.example.aerobankapp.workbench.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil
{
    public static void main(String[] args)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "89-48-42";
        String encoded = encoder.encode(rawPassword);
        System.out.println(encoded);
    }
}
