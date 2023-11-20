package com.example.aerobankapp.workbench.utilities;

public enum BankAuthorization
{
    CUSTOMER("Customer"),
    TELLER("Teller"),
    MANAGER("Manager"),
    ADMIN("Administrator");

   private String description;

   BankAuthorization(String code)
   {
       this.description = code;
   }

}
