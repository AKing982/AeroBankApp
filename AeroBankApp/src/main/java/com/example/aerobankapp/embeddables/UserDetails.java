package com.example.aerobankapp.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
public class UserDetails {

    @Column(name="firstName", nullable = false)
    @NotEmpty
    @NotBlank
    private String firstName;

    @Column(name="lastName", nullable = false)
    @NotEmpty
    @NotBlank
    private String lastName;

    @Column(name="email", nullable = false)
    @NotEmpty
    @NotBlank
    private String email;

    @Column(name="accountNumber", nullable = false)
    @NotBlank
    @NotEmpty
    private String accountNumber;

    @Column(name="profileImgUrl", nullable = false)
    @NotEmpty
    @NotBlank
    private String profileImgUrl;

    public UserDetails(String firstName, String lastName, String email, String accountNumber, String userProfileImg) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountNumber = accountNumber;
        this.profileImgUrl = userProfileImg;
    }

    public @NotEmpty @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty @NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty @NotBlank String lastName) {
        this.lastName = lastName;
    }

    public @NotEmpty @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank @NotEmpty String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(@NotBlank @NotEmpty String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public @NotEmpty @NotBlank String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(@NotEmpty @NotBlank String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }
}
