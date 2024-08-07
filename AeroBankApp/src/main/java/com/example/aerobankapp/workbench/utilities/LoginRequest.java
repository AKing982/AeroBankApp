package com.example.aerobankapp.workbench.utilities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest
{
    private String username;
    private String password;
}
