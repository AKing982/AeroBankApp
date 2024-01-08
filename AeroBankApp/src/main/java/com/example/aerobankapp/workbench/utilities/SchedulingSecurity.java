package com.example.aerobankapp.workbench.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class SchedulingSecurity
{
    private boolean isSchedulingAllowed;
}
