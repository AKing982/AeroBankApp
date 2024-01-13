package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum Role
{
    CUSTOMER("ROLE_USER"),
    TELLER("ROLE_TELLER"),
    MANAGER("ROLE_MANAGER"),
    AUDITOR("ROLE_AUDITOR"),
    ADMIN("ROLE_ADMIN");

   private String description;

   Role(String code)
   {
       this.description = code;
   }

}
