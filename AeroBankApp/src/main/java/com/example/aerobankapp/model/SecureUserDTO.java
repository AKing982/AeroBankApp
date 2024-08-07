package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import lombok.Builder;

@Builder
public record SecureUserDTO(
        UserDTO user,
        UserSecurityProfile userAuthority) {
}
