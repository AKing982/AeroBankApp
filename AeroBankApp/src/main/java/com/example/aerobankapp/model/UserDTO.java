package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.workbench.utilities.Role;
import lombok.*;
import org.springframework.stereotype.Component;


@Builder(access = AccessLevel.PUBLIC)
@Component
public class UserDTO
{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String pinNumber;
    private boolean isAdmin;
    private Role role;

    public UserDTO(String firstName, String lastName, String userName, String email, String password, String pinNumber, boolean isAdmin, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.isAdmin = isAdmin;
        this.role = role;
    }

    public UserDTO()
    {

    }

    public UserDTO(String user)
    {
        this.userName = user;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
