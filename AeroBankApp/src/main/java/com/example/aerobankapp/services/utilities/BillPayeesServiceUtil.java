package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.workbench.utilities.response.BillPayeesResponse;

import java.util.ArrayList;
import java.util.List;

public class BillPayeesServiceUtil
{
    public static List<BillPayeesResponse> getBillPayeeResponseList(List<BillPaymentEntity> billPayeesEntityList){
        List<BillPayeesResponse> billPayeesResponses = new ArrayList<>();
        for(BillPaymentEntity billPayees : billPayeesEntityList){
            if(billPayees != null){
                int id = Math.toIntExact(billPayees.getPaymentID());
                String name = billPayees.getPayeeName();
                BillPayeesResponse billPayeesResponse = new BillPayeesResponse(name, id);
                billPayeesResponses.add(billPayeesResponse);
            }
        }
        return billPayeesResponses;
    }
}
