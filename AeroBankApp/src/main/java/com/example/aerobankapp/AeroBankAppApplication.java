package com.example.aerobankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EntityScan(basePackages = "com.example.aerobankapp.entity")
public class AeroBankAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AeroBankAppApplication.class, args);
    }

}
