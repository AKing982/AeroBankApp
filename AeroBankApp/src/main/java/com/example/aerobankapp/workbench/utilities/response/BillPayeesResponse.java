package com.example.aerobankapp.workbench.utilities.response;

import com.example.aerobankapp.entity.BillPayeesEntity;
import lombok.Data;

@Data
public class BillPayeesResponse
{
    private String name;
    private int id;

    public BillPayeesResponse(String name, int id){
        this.name = name;
        this.id = id;
    }
}
