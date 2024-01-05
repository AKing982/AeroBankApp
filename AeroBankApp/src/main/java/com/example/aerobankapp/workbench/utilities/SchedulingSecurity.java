package com.example.aerobankapp.workbench.utilities;

public enum SchedulingSecurity
{
    SCHEDULING_ALLOWED("Scheduling Allowed");
    private String status;

    SchedulingSecurity(String code)
    {
        this.status = code;
    }
}
