package com.example.aerobankapp.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
public class UserCredentials {

    @Column(name="username")
    @NotBlank
    @NotEmpty
    @Size(min = 8, message = "You must choose at least 8 characters")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 24, max = 225, message = "You must choose at least 24 characters")
    @Column(name="password", nullable = false)
    private String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentials() {
    }

    public @NotBlank @NotEmpty @Size(min = 8, message = "You must choose at least 8 characters") String getUsername() {
        return username;
    }

    public @NotNull @NotBlank @Size(min = 24, max = 225, message = "You must choose at least 24 characters") String getPassword() {
        return password;
    }
}
