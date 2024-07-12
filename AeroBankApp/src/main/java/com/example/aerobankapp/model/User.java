package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }
}
