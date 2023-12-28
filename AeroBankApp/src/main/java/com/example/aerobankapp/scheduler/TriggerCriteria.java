package com.example.aerobankapp.scheduler;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
public record TriggerCriteria(int day,
                              int minute,
                              int hour,
                              int second,
                              int month,
                              int year,
                              int interval,
                              int repeat,
                              boolean isCron) {


}
