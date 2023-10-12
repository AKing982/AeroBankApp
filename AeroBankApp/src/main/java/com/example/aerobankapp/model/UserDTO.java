package com.example.aerobankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class UserDTO
{
    private int id;
    private String user;
    private String email;
    private String accountNumber;
    private char[] password;
    private int pinNumber;
    private boolean isAdmin;

    public UserDTO(UserDTOBuilder builder)
    {
        this.user = builder.userName;
        this.email = builder.email;
        this.accountNumber = builder.accountNumber;
        this.password = builder.password;
        this.pinNumber = builder.pinNumber;
        this.isAdmin = builder.isAdmin;
    }

    public static class UserDTOBuilder
    {
        private String userName;
        private String email;
        private String accountNumber;
        private char[] password;
        private int pinNumber;
        private boolean isAdmin;

        public UserDTOBuilder setUserName(String user)
        {
            this.userName = user;
            return this;
        }

        public UserDTOBuilder setEmail(String email)
        {
            this.email = email;
            return this;
        }

        public UserDTOBuilder setAccountNumber(String acctNo)
        {
            this.accountNumber = acctNo;
            return this;
        }

        public UserDTOBuilder setPassword(char[] pass)
        {
            this.password = pass;
            return this;
        }

        public UserDTOBuilder setPinNumber(int pin)
        {
            this.pinNumber = pin;
            return this;
        }

        public UserDTOBuilder isAdmin(boolean isAdmin)
        {
            this.isAdmin = isAdmin;
            return this;
        }

        public UserDTO build()
        {
            return new UserDTO(this);
        }
    }
}
