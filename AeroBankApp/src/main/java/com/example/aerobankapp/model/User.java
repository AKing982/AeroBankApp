package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Builder
@Data
public class User
{
    private int userID;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String pinNumber;
    private boolean isAdmin;
    private Role role;
    private AccountNumber accountNumber;

    public User(String firstName, String lastName, String username, String email, String password, String pinNumber, boolean isAdmin, Role role, AccountNumber accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.isAdmin = isAdmin;
        this.role = role;
        this.accountNumber = accountNumber;
    }

    public User()
    {

    }

    /**
     * This constructor is to be used in updating user properties
     * @param userID
     * @param firstName
     * @param lastName
     * @param username
     * @param email
     * @param password
     * @param pinNumber
     * @param isAdmin
     * @param role
     * @param accountNumber
     */
    public User(int userID, String firstName, String lastName, String username, String email, String password, String pinNumber, boolean isAdmin, Role role, AccountNumber accountNumber) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.isAdmin = isAdmin;
        this.role = role;
        this.accountNumber = accountNumber;
    }


}
