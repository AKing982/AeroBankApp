package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import lombok.Builder;

@Builder
public record SecureUserDTO(
        UserDTO user,
        UserAuthority userAuthority) {
}
