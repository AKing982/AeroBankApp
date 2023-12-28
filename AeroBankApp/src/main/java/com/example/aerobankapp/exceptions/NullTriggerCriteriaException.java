package com.example.aerobankapp.exceptions;

import com.example.aerobankapp.scheduler.TriggerCriteria;

public class NullTriggerCriteriaException extends NullPointerException
{
    public NullTriggerCriteriaException(String s) {
        super(s);
    }
}
