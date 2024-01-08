package com.example.aerobankapp.workbench.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatus
{
    private boolean isExpired;
    private boolean isEnabled;
    private boolean isLocked;
}
