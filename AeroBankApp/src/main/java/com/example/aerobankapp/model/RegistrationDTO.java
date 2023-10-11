package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Component
public class RegistrationDTO
{
    private String firstName;
    private String lastName;

    private String userName;

    private String email;
    private String address;
    private int zipcode;

    private int pinNumber;

    private char[] password;

    private BigDecimal deposit;

    private boolean isAdmin;

    public RegistrationDTO(RegistrationBuilder builder)
    {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.email = builder.email;
        this.address = builder.address;
        this.zipcode = builder.zipcode;
        this.pinNumber = builder.pinNumber;
        this.password = builder.password;
        this.deposit = builder.deposit;
        this.isAdmin = builder.isAdmin;
    }

    public static class RegistrationBuilder
    {
        private String firstName;
        private String lastName;

        private String userName;

        private String email;
        private String address;
        private int zipcode;

        private int pinNumber;

        private char[] password;

        private BigDecimal deposit;

        private boolean isAdmin;

        public RegistrationBuilder setFirstName(String first)
        {
            this.firstName = first;
            return this;
        }

        public RegistrationBuilder setLastName(String last)
        {
            this.lastName = last;
            return this;
        }

        public RegistrationBuilder setEmail(String email)
        {
            this.email = email;
            return this;
        }

        public RegistrationBuilder setAddress(String addr)
        {
            this.address = addr;
            return this;
        }

        public RegistrationBuilder setUserName(String user)
        {
            this.userName = user;
            return this;
        }

        public RegistrationBuilder setZipCode(int zip)
        {
            this.zipcode = zip;
            return this;
        }

        public RegistrationBuilder setPinNumber(int pin)
        {
            this.pinNumber = pin;
            return this;
        }

        public RegistrationBuilder setPassword(char[] pass)
        {
            this.password = pass;
            return this;
        }

        public RegistrationBuilder setDeposit(BigDecimal deposit)
        {
            this.deposit = deposit;
            return this;
        }

        public RegistrationBuilder isAdmin(boolean isAdmin)
        {
            this.isAdmin = isAdmin;
            return this;
        }

        public RegistrationDTO build()
        {
            return new RegistrationDTO(this);
        }

    }
}
