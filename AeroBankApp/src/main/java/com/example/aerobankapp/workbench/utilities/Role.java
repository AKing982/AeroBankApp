package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import org.springframework.stereotype.Component;


public enum Role
{
    USER("USER"),
    TELLER("TELLER"),
    MANAGER("MANAGER"),
    AUDITOR("AUDITOR"),
    ADMIN("ADMIN");

   private String description;

   Role(String code)
   {
       this.description = code;
   }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
