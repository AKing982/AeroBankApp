package com.example.aerobankapp.embeddables;

import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
public class UserSecurity {

    @Column(name="pinNumber")
    private String pinNumber;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="isAdmin")
    private boolean isAdmin;

    @Column(name="isEnabled")
    private boolean isEnabled;

    public UserSecurity(String pinNumber, Role role, boolean isAdmin, boolean isEnabled) {
        this.pinNumber = pinNumber;
        this.role = role;
        this.isAdmin = isAdmin;
        this.isEnabled = isEnabled;
    }
}
